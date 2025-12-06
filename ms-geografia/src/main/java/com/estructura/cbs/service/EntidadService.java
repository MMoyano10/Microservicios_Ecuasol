package com.estructura.cbs.service;

import com.estructura.cbs.dto.EntidadDTO;
import com.estructura.cbs.model.Entidad;
import com.estructura.cbs.repository.EntidadRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntidadService {

    private final EntidadRepository repository;

    public EntidadService(EntidadRepository repository) {
        this.repository = repository;
    }

    // Entity → DTO
    private EntidadDTO toDTO(Entidad e) {
        EntidadDTO dto = new EntidadDTO();
        dto.setEntidadId(e.getEntidadId());
        dto.setRuc(e.getRuc());
        dto.setRazonSocial(e.getRazonSocial());
        dto.setNombreComercial(e.getNombreComercial());
        dto.setEstado(e.getEstado());
        return dto;
    }

    // DTO → Entity
    private Entidad toEntity(EntidadDTO dto) {
        Entidad e = new Entidad();
        e.setEntidadId(dto.getEntidadId());
        e.setRuc(dto.getRuc());
        e.setRazonSocial(dto.getRazonSocial());
        e.setNombreComercial(dto.getNombreComercial());
        e.setEstado(dto.getEstado());
        return e;
    }

    public ResponseEntity<?> listar() {
        List<EntidadDTO> lista = repository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
        return ResponseEntity.ok(lista);
    }

    public ResponseEntity<?> buscarPorId(Integer id) {
        return repository.findById(id)
                .map(e -> ResponseEntity.ok(toDTO(e)))
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<?> guardar(EntidadDTO dto) {
        if (dto.getEntidadId() == null) {
            return ResponseEntity.badRequest().body("entidadId es obligatorio");
        }
        if (repository.existsById(dto.getEntidadId())) {
            return ResponseEntity.badRequest().body("Ya existe una entidad con ese ID");
        }
        if (repository.existsByRuc(dto.getRuc())) {
            return ResponseEntity.badRequest().body("Ya existe una entidad con ese RUC");
        }

        repository.save(toEntity(dto));
        return ResponseEntity.ok("Entidad registrada correctamente");
    }

    public ResponseEntity<?> actualizar(Integer id, EntidadDTO dto) {
        if (!repository.existsById(id)) {
            return ResponseEntity.badRequest().body("No existe una entidad con ese ID");
        }
        dto.setEntidadId(id);
        repository.save(toEntity(dto));
        return ResponseEntity.ok("Entidad actualizada correctamente");
    }

    public ResponseEntity<?> eliminar(Integer id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
