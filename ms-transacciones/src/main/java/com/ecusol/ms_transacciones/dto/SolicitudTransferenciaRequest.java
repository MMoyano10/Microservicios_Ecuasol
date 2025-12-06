package com.ecusol.ms_transacciones.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class SolicitudTransferenciaRequest {
    
    private Integer cuentaOrigenId;
    private BigDecimal monto;
    private String cuentaDestino; // Número de cuenta en el otro banco
    private String bancoDestinoBic; // Código BIC del banco destino (ej. "BANCO-B-BIC")
    private String descripcion;
    
    // Constructor vacío sin Lombok
    public SolicitudTransferenciaRequest() {
    }
}