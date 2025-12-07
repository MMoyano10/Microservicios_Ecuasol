package com.estructura.cbs.repository;

import com.estructura.cbs.model.UbicacionGeografica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UbicacionRepository extends JpaRepository<UbicacionGeografica, Integer> {

    List<UbicacionGeografica> findByUbicacionPadreId(Integer ubicacionPadreId);
}
