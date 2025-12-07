package com.ecusol.ms_clientes.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "representanteempresa")
@Getter
@Setter
public class RepresentanteEmpresa implements Serializable {

    // PK (en DB podría llamarse clienteempresaid, ajústalo si es necesario)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "clienteempresaid")
    private Integer representanteId;

    // FK a empresa (cliente empresa)
    @Column(name = "empresaid", nullable = false)
    private Integer empresaId;

    // FK a persona (cliente persona)
    @Column(name = "personaid", nullable = false)
    private Integer personaId;

    @Column(name = "rol", nullable = false, length = 50)
    private String rol;

    @Column(name = "fechainicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fechafin")
    private LocalDate fechaFin;

    @Column(name = "estado", nullable = false, length = 15)
    private String estado;

    // Constructor vacío
    public RepresentanteEmpresa() {
    }

    // Constructor solo con la clave primaria
    public RepresentanteEmpresa(Integer representanteId) {
        this.representanteId = representanteId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RepresentanteEmpresa that = (RepresentanteEmpresa) o;

        return representanteId != null && representanteId.equals(that.representanteId);
    }

    @Override
    public int hashCode() {
        return representanteId != null ? representanteId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "RepresentanteEmpresa{" +
                "representanteId=" + representanteId +
                ", empresaId=" + empresaId +
                ", personaId=" + personaId +
                ", rol='" + rol + '\'' +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", estado='" + estado + '\'' +
                '}';
    }
}