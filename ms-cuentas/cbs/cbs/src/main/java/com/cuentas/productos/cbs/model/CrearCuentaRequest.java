package com.cuentas.productos.cbs.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CrearCuentaRequest {

    @NotNull
    private Integer clienteId;

    @NotNull
    private Integer tipoCuentaId;

    @NotNull
    private Integer sucursalIdApertura;

    @Min(0)
    private BigDecimal saldoInicial;
}
