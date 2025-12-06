package com.estructura.cbs.repository;

import com.estructura.cbs.model.Feriado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeriadoRepository extends JpaRepository<Feriado, Integer> {

    List<Feriado> findByUbicacionId(Integer ubicacionId);
}
