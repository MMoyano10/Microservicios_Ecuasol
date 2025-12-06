package com.estructura.cbs.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Objects;

@Entity
@Table(name = "entidad")
@Getter
@Setter
public class Entidad {

    @Id
    @Column(name = "entidadid")
    private Integer entidadId;

    @Column(name = "ruc", nullable = false, length = 13)
    private String ruc;

    @Column(name = "razonsocial", nullable = false, length = 100)
    private String razonSocial;

    @Column(name = "nombrecomercial", nullable = false, length = 100)
    private String nombreComercial;

    @Column(name = "estado", nullable = false, length = 15)
    private String estado;

    public Entidad() {}

    public Entidad(Integer entidadId) {
        this.entidadId = entidadId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entidad)) return false;
        Entidad other = (Entidad) o;
        return entidadId != null && entidadId.equals(other.entidadId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entidadId);
    }

    @Override
    public String toString() {
        return "Entidad{" +
                "entidadId=" + entidadId +
                ", ruc='" + ruc + '\'' +
                ", razonSocial='" + razonSocial + '\'' +
                ", nombreComercial='" + nombreComercial + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}
