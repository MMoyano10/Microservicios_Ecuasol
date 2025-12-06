package com.cuentas.productos.cbs.controller;

import com.cuentas.productos.cbs.model.TasaInteresHistorico;
import com.cuentas.productos.cbs.service.TasaInteresService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/tasas")
public class TasaInteresController {

    private final TasaInteresService service;

    public TasaInteresController(TasaInteresService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<TasaInteresHistorico>> listarTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @GetMapping("/tipo/{tipoCuentaId}")
    public ResponseEntity<List<TasaInteresHistorico>> listarPorTipoCuenta(
            @PathVariable Integer tipoCuentaId) {
        return ResponseEntity.ok(service.listarPorTipoCuenta(tipoCuentaId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TasaInteresHistorico> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(service.obtener(id));
    }

    @PostMapping("/tipo/{tipoCuentaId}")
    public ResponseEntity<TasaInteresHistorico> crear(
            @PathVariable Integer tipoCuentaId,
            @RequestBody TasaInteresHistorico tasa) {
        TasaInteresHistorico creada = service.crear(tipoCuentaId, tasa);
        URI location = URI.create("/api/tasas/" + creada.getId());
        return ResponseEntity.created(location).body(creada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
