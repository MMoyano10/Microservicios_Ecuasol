package com.cuentas.productos.cbs.controller;

import com.cuentas.productos.cbs.entities.TipoCuenta;
import com.cuentas.productos.cbs.service.TipoCuentaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/tipos-cuenta")
public class TipoCuentaController {

    private final TipoCuentaService service;

    public TipoCuentaController(TipoCuentaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<TipoCuenta>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoCuenta> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(service.obtener(id));
    }

    @PostMapping
    public ResponseEntity<TipoCuenta> crear(@RequestBody TipoCuenta tipo) {
        TipoCuenta creado = service.crear(tipo);
        URI location = URI.create("/api/tipos-cuenta/" + creado.getId());
        return ResponseEntity.created(location).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoCuenta> actualizar(@PathVariable Integer id,
                                                 @RequestBody TipoCuenta tipo) {
        return ResponseEntity.ok(service.actualizar(id, tipo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
