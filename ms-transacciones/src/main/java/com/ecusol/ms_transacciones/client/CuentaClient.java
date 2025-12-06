package com.ecusol.ms_transacciones.client;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.Objects;

@Component
public class CuentaClient {

    private final WebClient webClient;

    // Inyectamos la URL base desde application.properties
    public CuentaClient(WebClient.Builder builder, @Value("${ms-cuentas.url}") String cuentasUrl) {
        this.webClient = builder.baseUrl(cuentasUrl).build();
    }

    /**
     * Consulta si la cuenta tiene saldo suficiente para la operación.
     * Retorna true si puede operar, false si no.
     */
    public boolean validarSaldo(Integer cuentaId, BigDecimal monto) {
        try {
            // Asumimos un endpoint en ms-cuentas: GET /api/cuentas/{id}/saldo-disponible
            Boolean tieneSaldo = webClient.get()
                    .uri("/api/cuentas/{id}/validar-monto?monto={monto}", cuentaId, monto)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block(); // Bloqueante por simplicidad en este paso
            
            return Boolean.TRUE.equals(tieneSaldo);
        } catch (Exception e) {
            // Si falla la conexión o la cuenta no existe, asumimos que no se puede operar
            System.err.println("Error consultando ms-cuentas: " + e.getMessage());
            return false;
        }
    }

    /**
     * Ejecuta el débito o crédito real en la cuenta del cliente.
     * Se llama cuando el Switch confirma la transacción (COMPLETED).
     */
    public void actualizarSaldo(Integer cuentaId, BigDecimal monto, String tipoMovimiento) {
        // TipoMovimiento: "DEBITO" o "CREDITO"
        MovimientoRequest request = new MovimientoRequest();
        request.setMonto(monto);
        request.setTipo(tipoMovimiento);
        request.setDescripcion("Transferencia Interbancaria Switch");

        try {
            webClient.post()
                    .uri("/api/cuentas/{id}/movimientos", cuentaId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
        } catch (Exception e) {
            // Esto es crítico: Si falla aquí, tendríamos una inconsistencia (Dinero enviado pero no descontado)
            // En un sistema real, aquí se lanzaría un evento de compensación (Saga Pattern).
            System.err.println("ALERTA CRÍTICA: No se pudo actualizar el saldo de la cuenta " + cuentaId);
            throw e;
        }
    }

    // --- DTO Interno para la petición a ms-cuentas ---
    @Getter
    @Setter
    public static class MovimientoRequest {
        private BigDecimal monto;
        private String tipo; // DEBITO, CREDITO
        private String descripcion;

        public MovimientoRequest() {}

        @Override
        public String toString() {
            return "MovimientoRequest{monto=" + monto + ", tipo='" + tipo + "', descripcion='" + descripcion + "'}";
        }
        
        // Equals y HashCode minimalistas
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MovimientoRequest that = (MovimientoRequest) o;
            return Objects.equals(monto, that.monto) && Objects.equals(tipo, that.tipo);
        }

        @Override
        public int hashCode() {
            return Objects.hash(monto, tipo);
        }
    }
}