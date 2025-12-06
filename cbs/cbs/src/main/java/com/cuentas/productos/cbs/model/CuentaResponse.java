package com.cuentas.productos.cbs.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class CuentaResponse {

    private Integer cuentaId;
    private String numeroCuenta;
    private Integer clienteId;
    private Integer tipoCuentaId;
    private Integer sucursalIdApertura;
    private BigDecimal saldo;
    private LocalDate fechaApertura;
    private String estado;

    public CuentaResponse() {
    }

    public CuentaResponse(Integer cuentaId,
                          String numeroCuenta,
                          Integer clienteId,
                          Integer tipoCuentaId,
                          Integer sucursalIdApertura,
                          BigDecimal saldo,
                          LocalDate fechaApertura,
                          String estado) {
        this.cuentaId = cuentaId;
        this.numeroCuenta = numeroCuenta;
        this.clienteId = clienteId;
        this.tipoCuentaId = tipoCuentaId;
        this.sucursalIdApertura = sucursalIdApertura;
        this.saldo = saldo;
        this.fechaApertura = fechaApertura;
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "CuentaResponse{" +
                "cuentaId=" + cuentaId +
                ", numeroCuenta='" + numeroCuenta + '\'' +
                ", clienteId=" + clienteId +
                ", tipoCuentaId=" + tipoCuentaId +
                ", sucursalIdApertura=" + sucursalIdApertura +
                ", saldo=" + saldo +
                ", fechaApertura=" + fechaApertura +
                ", estado='" + estado + '\'' +
                '}';
    }
}
