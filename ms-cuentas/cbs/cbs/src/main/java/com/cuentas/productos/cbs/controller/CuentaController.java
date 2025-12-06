package com.cuentas.productos.cbs.controller;

import com.cuentas.productos.cbs.model.CrearCuentaRequest;
import com.cuentas.productos.cbs.model.CuentaResponse;
import com.cuentas.productos.cbs.service.CuentaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {

    private final CuentaService cuentaService;

    public CuentaController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    @PostMapping
    public ResponseEntity<CuentaResponse> crear(@Valid @RequestBody CrearCuentaRequest request) {
        CuentaResponse creada = cuentaService.crearCuenta(request);
        URI location = URI.create("/api/cuentas/" + creada.getCuentaId());
        return ResponseEntity.created(location).body(creada);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CuentaResponse> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(cuentaService.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<CuentaResponse>> listarPorCliente(
            @RequestParam Integer clienteId) {
        return ResponseEntity.ok(cuentaService.listarPorCliente(clienteId));
    }
}
