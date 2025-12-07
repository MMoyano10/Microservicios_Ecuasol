package com.estructura.cbs.service;

import com.estructura.cbs.dto.FeriadoDTO;
import com.estructura.cbs.model.Feriado;
import com.estructura.cbs.repository.FeriadoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeriadoService {

    private final FeriadoRepository repository;

    public FeriadoService(FeriadoRepository repository) {
        this.repository = repository;
    }

    private FeriadoDTO toDTO(Feriado f) {
        FeriadoDTO dto = new FeriadoDTO();
        dto.setFeriadoId(f.getFeriadoId());
        dto.setUbicacionId(f.getUbicacionId());
        dto.setFecha(f.getFecha());
        dto.setDescripcion(f.getDescripcion());
        return dto;
    }

    private Feriado toEntity(FeriadoDTO dto) {
        Feriado f = new Feriado();
        f.setFeriadoId(dto.getFeriadoId());
        f.setUbicacionId(dto.getUbicacionId());
        f.setFecha(dto.getFecha());
        f.setDescripcion(dto.getDescripcion());
        return f;
    }

    public ResponseEntity<?> listar() {
        List<FeriadoDTO> lista =
                repository.findAll().stream().map(this::toDTO).toList();
        return ResponseEntity.ok(lista);
    }

    public ResponseEntity<?> buscarPorId(Integer id) {
        return repository.findById(id)
                .map(f -> ResponseEntity.ok(toDTO(f)))
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<?> listarPorUbicacion(Integer ubicacionId) {
        List<FeriadoDTO> lista =
                repository.findByUbicacionId(ubicacionId)
                        .stream().map(this::toDTO).toList();
        return ResponseEntity.ok(lista);
    }

    public ResponseEntity<?> guardar(FeriadoDTO dto) {
        if (dto.getFeriadoId() == null) {
            return ResponseEntity.badRequest().body("feriadoId es obligatorio");
        }
        if (repository.existsById(dto.getFeriadoId())) {
            return ResponseEntity.badRequest().body("Ya existe un feriado con ese ID");
        }
        repository.save(toEntity(dto));
        return ResponseEntity.ok("Feriado registrado correctamente");
    }

    public ResponseEntity<?> actualizar(Integer id, FeriadoDTO dto) {
        if (!repository.existsById(id)) {
            return ResponseEntity.badRequest().body("No existe un feriado con ese ID");
        }
        dto.setFeriadoId(id);
        repository.save(toEntity(dto));
        return ResponseEntity.ok("Feriado actualizado correctamente");
    }

    public ResponseEntity<?> eliminar(Integer id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
