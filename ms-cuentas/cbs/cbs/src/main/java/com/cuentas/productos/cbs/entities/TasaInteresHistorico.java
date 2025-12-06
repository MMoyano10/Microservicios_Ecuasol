package com.cuentas.productos.cbs.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "tasaintereshistorico")
@Getter
@Setter
@NoArgsConstructor
public class TasaInteresHistorico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tasaintereshistoricoid")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "tipocuentaid", nullable = false)
    private TipoCuenta tipoCuenta;

    @Column(name = "tasamensual", nullable = false)
    private Double tasaMensual;

    @Column(name = "fechainiciovigencia", nullable = false)
    private LocalDate fechaInicioVigencia;

    @Column(name = "fechafinvigencia")
    private LocalDate fechaFinVigencia;
}

