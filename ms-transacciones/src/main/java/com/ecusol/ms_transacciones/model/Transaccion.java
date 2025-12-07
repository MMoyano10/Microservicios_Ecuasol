package com.ecusol.ms_transacciones.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "transaccion")
@Getter
@Setter
public class Transaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaccionid", nullable = false)
    private Integer transaccionId; // CAMBIO: de 'transaccionid' a 'transaccionId'

    @Column(name = "cuentaid", nullable = false)
    private Integer cuentaId; // CAMBIO: de 'cuentaid' a 'cuentaId' (Esto arregla el error)

    @Column(name = "referencia", length = 100)
    private String referencia;

    @Column(name = "roltransaccion", length = 20)
    private String rolTransaccion;

    @Column(name = "monto", precision = 19, scale = 2, nullable = false)
    private BigDecimal monto;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @Column(name = "estado", length = 20, nullable = false)
    private String estado;

    @Column(name = "fechaejecucion")
    private LocalDateTime fechaEjecucion;

    // Constructor vacío
    public Transaccion() {
    }

    // Constructor solo con clave primaria
    public Transaccion(Integer transaccionId) {
        this.transaccionId = transaccionId;
    }

    // Equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaccion that = (Transaccion) o;
        return Objects.equals(transaccionId, that.transaccionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transaccionId);
    }

    // ToString actualizado con los nuevos nombres
    @Override
    public String toString() {
        return "Transaccion{" +
                "transaccionId=" + transaccionId +
                ", cuentaId=" + cuentaId +
                ", referencia='" + referencia + '\'' +
                ", rolTransaccion='" + rolTransaccion + '\'' +
                ", monto=" + monto +
                ", descripcion='" + descripcion + '\'' +
                ", estado='" + estado + '\'' +
                ", fechaEjecucion=" + fechaEjecucion +
                '}';
    }
}