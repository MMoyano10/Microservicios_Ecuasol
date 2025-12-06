package com.cuentas.productos.cbs.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "cuenta")
@Getter
@Setter
@NoArgsConstructor
public class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cuentaid")
    private Integer id;

    @Column(name = "clienteid", nullable = false)
    private Integer clienteId;                 // ID de otro microservicio

    @ManyToOne
    @JoinColumn(name = "tipocuentaid", nullable = false)
    private TipoCuenta tipoCuenta;

    @Column(name = "sucursalidapertura", nullable = false)
    private Integer sucursalIdApertura;       // ID de otro microservicio

    @Column(name = "numerocuenta", nullable = false, unique = true, length = 20)
    private String numeroCuenta;

    @Column(name = "saldo", nullable = false)
    private BigDecimal saldo;

    @Column(name = "fechaapertura", nullable = false)
    private LocalDate fechaApertura;

    @Column(name = "estado", nullable = false, length = 15)
    private String estado;
}
