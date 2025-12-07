package com.ecusol.ms_transacciones.service;

import com.ecusol.ms_transacciones.client.CuentaClient;
import com.ecusol.ms_transacciones.dto.SolicitudTransferenciaRequest;
import com.ecusol.ms_transacciones.dto.hermes.SolicitudSwitchWrapper;
import com.ecusol.ms_transacciones.dto.hermes.TransaccionSwitchDTO;
import com.ecusol.ms_transacciones.model.Transaccion;
import com.ecusol.ms_transacciones.repository.TransaccionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransaccionService {

    private final TransaccionRepository transaccionRepository;
    private final CuentaClient cuentaClient;
    
    @Qualifier("webClient") 
    private final WebClient webClientHermes; 

    // --- ENVIAR DINERO (Cumpliendo el PDF) ---
    public Transaccion realizarTransferencia(SolicitudTransferenciaRequest request) {
        
        // 1. Validar Saldo Local
        boolean tieneSaldo = cuentaClient.validarSaldo(request.getCuentaOrigenId(), request.getMonto());
        if (!tieneSaldo) {
            throw new RuntimeException("Fondos insuficientes");
        }

        // 2. Crear registro local (PENDING)
        Transaccion tx = new Transaccion();
        tx.setCuentaId(request.getCuentaOrigenId());
        tx.setMonto(request.getMonto());
        tx.setRolTransaccion("DEBITO");
        String e2e = "E2E-" + UUID.randomUUID().toString().substring(0, 8);
        tx.setReferencia(e2e);
        tx.setDescripcion(request.getDescripcion());
        tx.setFechaEjecucion(LocalDateTime.now());
        tx.setEstado("PENDING");
        Transaccion txGuardada = transaccionRepository.save(tx);

        // 3. Mapeo al JSON del PDF
        Integer idBancoDestino = 1; // Por defecto a ArcBank
        try { idBancoDestino = Integer.parseInt(request.getBancoDestinoBic()); } catch (Exception e) {}

        TransaccionSwitchDTO dto = TransaccionSwitchDTO.builder()
                .idInstruccion(null)
                .endToEnd(e2e)
                .idBancoOrigen(2) // 2 = EcuSol
                .idBancoDestino(idBancoDestino)
                .cuentaOrigen(request.getCuentaOrigenId().toString())
                .cuentaDestino(request.getCuentaDestino())
                .monto(request.getMonto())
                .mensaje(request.getDescripcion())
                .estadoActual("Enviado")
                .fechaCreacion(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .codigoRespuestaFinal(null)
                .build();

        SolicitudSwitchWrapper wrapper = new SolicitudSwitchWrapper(dto);

        // 4. Descontar Saldo
        cuentaClient.actualizarSaldo(request.getCuentaOrigenId(), request.getMonto(), "DEBITO");

        // 5. Enviar al Switch
        try {
            webClientHermes.post()
                    .uri("/api/transferencias") // Ajustar según Switch real
                    .bodyValue(wrapper)
                    .retrieve()
                    .bodyToMono(String.class) // Recibimos la respuesta raw por ahora
                    .block();

            txGuardada.setEstado("COMPLETED");
            transaccionRepository.save(txGuardada);

        } catch (Exception e) {
            txGuardada.setEstado("FAILED");
            // Aquí iría lógica de reverso (devolver dinero)
            transaccionRepository.save(txGuardada);
            System.err.println("Error Switch: " + e.getMessage());
        }

        return txGuardada;
    }

    // --- RECIBIR DINERO (Webhook / Crédito) ---
    public Transaccion recibirTransferencia(SolicitudSwitchWrapper wrapper) {
        TransaccionSwitchDTO datos = wrapper.getTransaccion();

        // Extraer datos del JSON del PDF
        // Asumimos que Cuenta Destino es nuestro ID de cliente local
        Integer cuentaIdLocal = Integer.parseInt(datos.getCuentaDestino()); 
        BigDecimal monto = datos.getMonto();

        // Acreditar
        cuentaClient.actualizarSaldo(cuentaIdLocal, monto, "CREDITO");

        // Guardar
        Transaccion tx = new Transaccion();
        tx.setCuentaId(cuentaIdLocal);
        tx.setMonto(monto);
        tx.setRolTransaccion("CREDITO");
        tx.setReferencia(datos.getEndToEnd());
        tx.setDescripcion(datos.getMensaje());
        tx.setFechaEjecucion(LocalDateTime.now());
        tx.setEstado("COMPLETED");

        return transaccionRepository.save(tx);
    }
}