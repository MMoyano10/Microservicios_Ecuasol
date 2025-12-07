package com.estructura.cbs.repository;

import com.estructura.cbs.model.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SucursalRepository extends JpaRepository<Sucursal, Integer> {

    List<Sucursal> findByEntidadId(Integer entidadId);

    List<Sucursal> findByUbicacionId(Integer ubicacionId);
}
