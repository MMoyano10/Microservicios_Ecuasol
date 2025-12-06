package com.ecusol.ms_transacciones.repository;

import com.ecusol.ms_transacciones.model.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransaccionRepository extends JpaRepository<Transaccion, Integer> {
    
    // Método para buscar transacciones por el ID de idempotencia (Instruction-ID)
    // Útil para verificar si ya procesamos una transacción antes de enviarla o al recibirla.
    Optional<Transaccion> findByReferencia(String referencia);
}