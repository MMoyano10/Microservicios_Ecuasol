package com.ecusol.ms_transacciones.service;

import com.ecusol.ms_transacciones.dto.hermes.SwitchTransferRequest;
import com.ecusol.ms_transacciones.dto.hermes.SwitchTransferResponse;
import com.ecusol.ms_transacciones.model.Transaccion;
import com.ecusol.ms_transacciones.repository.TransaccionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransaccionService {

    private final TransaccionRepository transaccionRepository;
    private final WebClient webClient;

    // TODO: Inyectar CuentaClient para validar saldo antes de iniciar (Paso 3.1.1 del plan)

    public Transaccion realizarTransferencia(Integer cuentaOrigenId, BigDecimal monto, String cuentaDestino, String bancoDestinoBic) {
        // 1. Crear la transacción local (Estado Inicial)
        Transaccion tx = new Transaccion();
        tx.setCuentaid(cuentaOrigenId);
        tx.setMonto(monto);
        tx.setReferencia(UUID.randomUUID().toString()); // Instruction-ID para Idempotencia RF-03
        tx.setRolTransaccion("DEBITO");
        tx.setEstado("RECEIVED"); // Estado inicial según diagrama de estados
        tx.setFechaEjecucion(LocalDateTime.now());
        tx.setDescripcion("Transferencia a " + bancoDestinoBic);
        
        // Guardamos en BD antes de llamar al Switch
        transaccionRepository.save(tx);

        try {
            // 2. Construir el Request para Hermes (Mapping)
            SwitchTransferRequest request = mapToSwitchRequest(tx, cuentaDestino, bancoDestinoBic);

            // 3. Llamada al Switch (RF-01)
            // Endpoint: POST /api/v2/switch/transfers 
            SwitchTransferResponse response = webClient.post()
                    .uri("/api/v2/switch/transfers")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(SwitchTransferResponse.class)
                    .block(); // Bloqueante para simplificar, en reactivo puro sería subscribe

            // 4. Manejo de Respuesta Exitosa (200 OK) [cite: 304]
            if (response != null && "COMPLETED".equals(response.getBody().getStatus())) {
                tx.setEstado("COMPLETED");
            } else {
                tx.setEstado("PENDING"); // O revisar lógica de estados intermedios
            }

        } catch (WebClientResponseException e) {
            // Manejo de errores 4xx/5xx (RF-06 Normalización)
            tx.setEstado("FAILED");
            System.err.println("Error en Switch: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            tx.setEstado("FAILED");
            System.err.println("Error interno: " + e.getMessage());
        }

        // 5. Actualizar estado final en BD
        return transaccionRepository.save(tx);
    }

    // Método auxiliar para mapear la Entidad al DTO de Hermes
    private SwitchTransferRequest mapToSwitchRequest(Transaccion tx, String cuentaDestino, String bancoDestinoBic) {
        SwitchTransferRequest req = new SwitchTransferRequest();

        // Header
        SwitchTransferRequest.Header header = new SwitchTransferRequest.Header();
        header.setMessageId("MSG-" + UUID.randomUUID());
        header.setOriginatingBankId("ECUSOL-BIC"); // Nuestro ID en la red
        header.setCreationDateTime(LocalDateTime.now().atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT));
        req.setHeader(header);

        // Body
        SwitchTransferRequest.Body body = new SwitchTransferRequest.Body();
        body.setInstructionId(tx.getReferencia()); // CLAVE CRÍTICA RF-03
        body.setEndToEndId("E2E-" + tx.getTransaccionid());
        body.setRemittanceInformation(tx.getDescripcion());

        // Amount
        SwitchTransferRequest.Amount amount = new SwitchTransferRequest.Amount();
        amount.setCurrency("USD");
        amount.setValue(tx.getMonto());
        body.setAmount(amount);

        // Debtor (Origen - Nosotros)
        SwitchTransferRequest.Debtor debtor = new SwitchTransferRequest.Debtor();
        debtor.setAccountId(tx.getCuentaid().toString());
        debtor.setName("Cliente EcuSol"); // Deberíamos buscar nombre real con CuentaClient
        debtor.setAccountType("SAVINGS");
        body.setDebtor(debtor);

        // Creditor (Destino - Otro Banco)
        SwitchTransferRequest.Creditor creditor = new SwitchTransferRequest.Creditor();
        creditor.setAccountId(cuentaDestino);
        creditor.setTargetBankId(bancoDestinoBic); // Vital para Enrutamiento RF-02
        creditor.setName("Beneficiario Externo");
        body.setCreditor(creditor);

        req.setBody(body);
        return req;
    }

    /**
     * Procesa una transferencia entrante desde el Switch (Webhook).
     * Corresponde a la sección 4.3 del PDF.
     */
    public void procesarRecepcion(SwitchTransferRequest request) {
        // 1. Validar Idempotencia (Opcional pero recomendado): 
        // Verificar si request.getBody().getInstructionId() ya existe en BD.
        if (transaccionRepository.findByReferencia(request.getBody().getInstructionId()).isPresent()) {
            return; // Ya procesado, retornamos 200 OK (Idempotencia)
        }

        // 2. Crear la transacción local (Rol CREDITO)
        Transaccion tx = new Transaccion();
        
        // En un escenario real, 'debtor' es quien envía, pero para nuestra BD local,
        // la cuenta afectada es la nuestra (creditor).
        // Convertimos el AccountId del request (String) al ID interno (Integer).
        // OJO: Aquí deberíamos validar que la cuenta exista en nuestro Core de Cuentas.
        try {
            tx.setCuentaid(Integer.parseInt(request.getBody().getCreditor().getAccountId()));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Cuenta destino inválida: " + request.getBody().getCreditor().getAccountId());
        }

        tx.setMonto(request.getBody().getAmount().getValue());
        tx.setReferencia(request.getBody().getInstructionId()); // Guardamos el ID del Switch
        tx.setRolTransaccion("CREDITO"); // Es un abono
        tx.setEstado("COMPLETED"); // Si llegamos aquí, aceptamos el dinero
        tx.setFechaEjecucion(LocalDateTime.now());
        tx.setDescripcion("Recibido de: " + request.getBody().getDebtor().getName());

        // 3. Persistir
        transaccionRepository.save(tx);
        
        // TODO: Llamar al ms-cuentas para aumentar el saldo real de la cuenta.
    }
}