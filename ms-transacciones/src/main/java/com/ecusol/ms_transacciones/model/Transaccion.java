package com.ecusol.ms_transacciones.model;

import jakarta.persistence.*; // O javax.persistence si usas Spring Boot < 3
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
    private Integer transaccionid;

    @Column(name = "cuentaid", nullable = false)
    private Integer cuentaid;

    @Column(name = "referencia", length = 100)
    private String referencia; // Mapeará al Instruction-ID del Switch (UUID)

    @Column(name = "roltransaccion", length = 20)
    private String rolTransaccion; // "DEBITO" (Origen) o "CREDITO" (Destino)

    @Column(name = "monto", precision = 19, scale = 2, nullable = false)
    private BigDecimal monto;

    @Column(name = "descripcion", length = 255)
    private String descripcion; // Remittance Information

    @Column(name = "estado", length = 20, nullable = false)
    private String estado; // RECEIVED, COMPLETED, FAILED, PENDING (Según ERS Switch)

    @Column(name = "fechaejecucion")
    private LocalDateTime fechaEjecucion;

    // Constructor vacío sin Lombok
    public Transaccion() {
    }

    // Constructor solo con clave primaria sin Lombok
    public Transaccion(Integer transaccionid) {
        this.transaccionid = transaccionid;
    }

    // Equals y HashCode comparando solo clave primaria sin Lombok
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaccion that = (Transaccion) o;
        return Objects.equals(transaccionid, that.transaccionid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transaccionid);
    }

    // ToString con todas las propiedades sin Lombok
    @Override
    public String toString() {
        return "Transaccion{" +
                "transaccionid=" + transaccionid +
                ", cuentaid=" + cuentaid +
                ", referencia='" + referencia + '\'' +
                ", rolTransaccion='" + rolTransaccion + '\'' +
                ", monto=" + monto +
                ", descripcion='" + descripcion + '\'' +
                ", estado='" + estado + '\'' +
                ", fechaEjecucion=" + fechaEjecucion +
                '}';
    }
}