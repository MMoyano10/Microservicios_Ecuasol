package com.cuentas.productos.cbs.model;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class MovimientoRequest {
    private BigDecimal monto;
    private String tipo; // "DEBITO" o "CREDITO"
    private String descripcion;
}