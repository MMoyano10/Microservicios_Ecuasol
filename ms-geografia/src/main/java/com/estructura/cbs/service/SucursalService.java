package com.estructura.cbs.service;

import com.estructura.cbs.dto.SucursalDTO;
import com.estructura.cbs.model.Sucursal;
import com.estructura.cbs.repository.SucursalRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SucursalService {

    private final SucursalRepository repository;

    public SucursalService(SucursalRepository repository) {
        this.repository = repository;
    }

    private SucursalDTO toDTO(Sucursal s) {
        SucursalDTO dto = new SucursalDTO();
        dto.setSucursalId(s.getSucursalId());
        dto.setEntidadId(s.getEntidadId());
        dto.setUbicacionId(s.getUbicacionId());
        dto.setCodigoSucursal(s.getCodigoSucursal());
        dto.setNombre(s.getNombre());
        dto.setDireccion(s.getDireccion());
        dto.setTelefono(s.getTelefono());
        dto.setLatitud(s.getLatitud());
        dto.setLongitud(s.getLongitud());
        dto.setEstado(s.getEstado());
        return dto;
    }

    private Sucursal toEntity(SucursalDTO dto) {
        Sucursal s = new Sucursal();
        s.setSucursalId(dto.getSucursalId());
        s.setEntidadId(dto.getEntidadId());
        s.setUbicacionId(dto.getUbicacionId());
        s.setCodigoSucursal(dto.getCodigoSucursal());
        s.setNombre(dto.getNombre());
        s.setDireccion(dto.getDireccion());
        s.setTelefono(dto.getTelefono());
        s.setLatitud(dto.getLatitud());
        s.setLongitud(dto.getLongitud());
        s.setEstado(dto.getEstado());
        return s;
    }

    public ResponseEntity<?> listar() {
        List<SucursalDTO> lista = repository.findAll()
                .stream().map(this::toDTO).toList();
        return ResponseEntity.ok(lista);
    }

    public ResponseEntity<?> buscarPorId(Integer id) {
        return repository.findById(id)
                .map(s -> ResponseEntity.ok(toDTO(s)))
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<?> listarPorEntidad(Integer entidadId) {
        List<SucursalDTO> lista =
                repository.findByEntidadId(entidadId)
                        .stream().map(this::toDTO).toList();
        return ResponseEntity.ok(lista);
    }

    public ResponseEntity<?> listarPorUbicacion(Integer ubicacionId) {
        List<SucursalDTO> lista =
                repository.findByUbicacionId(ubicacionId)
                        .stream().map(this::toDTO).toList();
        return ResponseEntity.ok(lista);
    }

    public ResponseEntity<?> guardar(SucursalDTO dto) {
        if (dto.getSucursalId() == null) {
            return ResponseEntity.badRequest().body("sucursalId es obligatorio");
        }
        if (repository.existsById(dto.getSucursalId())) {
            return ResponseEntity.badRequest().body("Ya existe una sucursal con ese ID");
        }
        repository.save(toEntity(dto));
        return ResponseEntity.ok("Sucursal registrada correctamente");
    }

    public ResponseEntity<?> actualizar(Integer id, SucursalDTO dto) {
        if (!repository.existsById(id)) {
            return ResponseEntity.badRequest().body("No existe una sucursal con ese ID");
        }
        dto.setSucursalId(id);
        repository.save(toEntity(dto));
        return ResponseEntity.ok("Sucursal actualizada correctamente");
    }

    public ResponseEntity<?> eliminar(Integer id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
