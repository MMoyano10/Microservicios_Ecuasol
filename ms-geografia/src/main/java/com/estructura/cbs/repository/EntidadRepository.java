package com.estructura.cbs.repository;

import com.estructura.cbs.model.Entidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntidadRepository extends JpaRepository<Entidad, Integer> {

    boolean existsByRuc(String ruc);
}
