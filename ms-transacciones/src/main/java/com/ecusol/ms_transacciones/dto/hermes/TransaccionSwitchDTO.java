package com.ecusol.ms_transacciones.dto.hermes;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class TransaccionSwitchDTO {

    @JsonProperty("IdInstruccion")
    private String idInstruccion; // null al inicio

    @JsonProperty("EndToEnd")
    private String endToEnd; // E2E-123456789

    @JsonProperty("IdBancoOrigen")
    private Integer idBancoOrigen; // EcuSol = 2

    @JsonProperty("IdBancoDestino") 
    private Integer idBancoDestino;

    @JsonProperty("CuentaOrigen")
    private String cuentaOrigen;

    @JsonProperty("CuentaDestino") 
    private String cuentaDestino;

    @JsonProperty("Monto")
    private BigDecimal monto;

    @JsonProperty("Mensaje")
    private String mensaje;

    @JsonProperty("EstadoActual")
    private String estadoActual;

    @JsonProperty("FechaCreacion")
    private String fechaCreacion; // ISO 8601

    @JsonProperty("CodigoRespuestaFinal") 
    private String codigoRespuestaFinal;
}