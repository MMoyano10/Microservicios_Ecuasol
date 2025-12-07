package com.estructura.cbs.service;

import com.estructura.cbs.dto.UbicacionGeograficaDTO;
import com.estructura.cbs.model.UbicacionGeografica;
import com.estructura.cbs.repository.UbicacionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UbicacionService {

    private final UbicacionRepository repository;

    public UbicacionService(UbicacionRepository repository) {
        this.repository = repository;
    }

    private UbicacionGeograficaDTO toDTO(UbicacionGeografica u) {
        UbicacionGeograficaDTO dto = new UbicacionGeograficaDTO();
        dto.setUbicacionId(u.getUbicacionId());
        dto.setNombre(u.getNombre());
        dto.setTipoUbicacion(u.getTipoUbicacion());
        dto.setUbicacionPadreId(u.getUbicacionPadreId());
        return dto;
    }

    private UbicacionGeografica toEntity(UbicacionGeograficaDTO dto) {
        UbicacionGeografica u = new UbicacionGeografica();
        u.setUbicacionId(dto.getUbicacionId());
        u.setNombre(dto.getNombre());
        u.setTipoUbicacion(dto.getTipoUbicacion());
        u.setUbicacionPadreId(dto.getUbicacionPadreId());
        return u;
    }

    public ResponseEntity<?> listar() {
        List<UbicacionGeograficaDTO> lista =
                repository.findAll().stream().map(this::toDTO).toList();
        return ResponseEntity.ok(lista);
    }

    public ResponseEntity<?> listarPorPadre(Integer padreId) {
        List<UbicacionGeograficaDTO> lista =
                repository.findByUbicacionPadreId(padreId)
                        .stream().map(this::toDTO).toList();
        return ResponseEntity.ok(lista);
    }

    public ResponseEntity<?> buscarPorId(Integer id) {
        return repository.findById(id)
                .map(u -> ResponseEntity.ok(toDTO(u)))
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<?> guardar(UbicacionGeograficaDTO dto) {
        if (dto.getUbicacionId() == null) {
            return ResponseEntity.badRequest().body("ubicacionId es obligatorio");
        }
        if (repository.existsById(dto.getUbicacionId())) {
            return ResponseEntity.badRequest().body("Ya existe una ubicación con ese ID");
        }
        repository.save(toEntity(dto));
        return ResponseEntity.ok("Ubicación registrada correctamente");
    }

    public ResponseEntity<?> actualizar(Integer id, UbicacionGeograficaDTO dto) {
        if (!repository.existsById(id)) {
            return ResponseEntity.badRequest().body("No existe una ubicación con ese ID");
        }
        dto.setUbicacionId(id);
        repository.save(toEntity(dto));
        return ResponseEntity.ok("Ubicación actualizada correctamente");
    }

    public ResponseEntity<?> eliminar(Integer id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
