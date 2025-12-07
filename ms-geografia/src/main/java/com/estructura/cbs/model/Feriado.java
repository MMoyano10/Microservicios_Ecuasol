package com.estructura.cbs.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "feriado")
@Getter
@Setter
public class Feriado {

    @Id
    @Column(name = "feriadoid")
    private Integer feriadoId;

    @Column(name = "ubicacionid", nullable = false)
    private Integer ubicacionId;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "descripcion", nullable = false, length = 100)
    private String descripcion;

    public Feriado() {}

    public Feriado(Integer feriadoId) {
        this.feriadoId = feriadoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Feriado)) return false;
        Feriado other = (Feriado) o;
        return feriadoId != null && feriadoId.equals(other.feriadoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(feriadoId);
    }

    @Override
    public String toString() {
        return "Feriado{" +
                "feriadoId=" + feriadoId +
                ", ubicacionId=" + ubicacionId +
                ", fecha=" + fecha +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
