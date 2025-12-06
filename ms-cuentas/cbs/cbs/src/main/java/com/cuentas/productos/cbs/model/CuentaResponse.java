package com.cuentas.productos.cbs.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CuentaResponse {
    private Integer cuentaId;
    private String numeroCuenta;
    private Integer clienteId;
    private Integer tipoCuentaId;
    private Integer sucursalIdApertura;
    private BigDecimal saldo;
    private LocalDate fechaApertura;
    private String estado;
}
