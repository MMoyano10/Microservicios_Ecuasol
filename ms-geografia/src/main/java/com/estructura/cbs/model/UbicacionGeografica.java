package com.estructura.cbs.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Objects;

@Entity
@Table(name = "ubicaciongeografica")
@Getter
@Setter
public class UbicacionGeografica {

    @Id
    @Column(name = "ubicacionid")
    private Integer ubicacionId;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "tipoubicacion", nullable = false, length = 15)
    private String tipoUbicacion;

    @Column(name = "ubicacionpadreid")
    private Integer ubicacionPadreId;

    public UbicacionGeografica() {}

    public UbicacionGeografica(Integer ubicacionId) {
        this.ubicacionId = ubicacionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UbicacionGeografica)) return false;
        UbicacionGeografica other = (UbicacionGeografica) o;
        return ubicacionId != null && ubicacionId.equals(other.ubicacionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ubicacionId);
    }

    @Override
    public String toString() {
        return "UbicacionGeografica{" +
                "ubicacionId=" + ubicacionId +
                ", nombre='" + nombre + '\'' +
                ", tipoUbicacion='" + tipoUbicacion + '\'' +
                ", ubicacionPadreId=" + ubicacionPadreId +
                '}';
    }
}
