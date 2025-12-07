package com.estructura.cbs.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "sucursal")
@Getter
@Setter
public class Sucursal {

    @Id
    @Column(name = "sucursalid")
    private Integer sucursalId;

    @Column(name = "entidadid", nullable = false)
    private Integer entidadId;

    @Column(name = "ubicacionid", nullable = false)
    private Integer ubicacionId;

    @Column(name = "codigosucursal", unique = true, nullable = false, length = 10)
    private String codigoSucursal;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "direccion", nullable = false, length = 200)
    private String direccion;

    @Column(name = "telefono", nullable = false, length = 20)
    private String telefono;

    @Column(name = "latitud", nullable = false)
    private BigDecimal latitud;

    @Column(name = "longitud", nullable = false)
    private BigDecimal longitud;

    @Column(name = "estado", nullable = false, length = 15)
    private String estado;

    public Sucursal() {}

    public Sucursal(Integer sucursalId) {
        this.sucursalId = sucursalId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sucursal)) return false;
        Sucursal other = (Sucursal) o;
        return sucursalId != null && sucursalId.equals(other.sucursalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sucursalId);
    }

    @Override
    public String toString() {
        return "Sucursal{" +
                "sucursalId=" + sucursalId +
                ", entidadId=" + entidadId +
                ", ubicacionId=" + ubicacionId +
                ", codigoSucursal='" + codigoSucursal + '\'' +
                ", nombre='" + nombre + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefono='" + telefono + '\'' +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", estado='" + estado + '\'' +
                '}';
    }
}
