package com.cuentas.productos.cbs.controller;

import com.cuentas.productos.cbs.entities.Cuenta;
import com.cuentas.productos.cbs.model.CrearCuentaRequest;
import com.cuentas.productos.cbs.model.CuentaResponse;
import com.cuentas.productos.cbs.model.MovimientoRequest; // Importamos el nuevo DTO
import com.cuentas.productos.cbs.repository.CuentaRepository; // Importamos el repo
import com.cuentas.productos.cbs.service.CuentaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/cuentas")
public class CuentaController {

    private final CuentaService cuentaService;
    private final CuentaRepository cuentaRepository; // Inyectamos el repositorio

    // Constructor actualizado para recibir el Service Y el Repository
    public CuentaController(CuentaService cuentaService, CuentaRepository cuentaRepository) {
        this.cuentaService = cuentaService;
        this.cuentaRepository = cuentaRepository;
    }

    // -------------------------------------------------------------------
    // MÉTODOS ORIGINALES DE TU COMPAÑERA (Intactos)
    // -------------------------------------------------------------------

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

    // -------------------------------------------------------------------
    // NUEVOS MÉTODOS PARA INTEGRACIÓN CON MS-TRANSACCIONES
    // -------------------------------------------------------------------

    /**
     * Endpoint 1: Validar Saldo
     * URL: GET /api/cuentas/{id}/validar-monto?monto=100.00
     */
    @GetMapping("/{id}/validar-monto")
    public ResponseEntity<Boolean> validarSaldo(@PathVariable Integer id, @RequestParam BigDecimal monto) {
        return cuentaRepository.findById(id)
                .map(cuenta -> {
                    // Comparamos el saldo actual con el monto solicitado
                    // compareTo devuelve >= 0 si el saldo es mayor o igual al monto
                    boolean tieneSaldo = cuenta.getSaldo().compareTo(monto) >= 0;
                    return ResponseEntity.ok(tieneSaldo);
                })
                .orElse(ResponseEntity.ok(false)); // Si no existe la cuenta, retorna false
    }

    /**
     * Endpoint 2: Ejecutar Movimiento (Débito/Crédito)
     * URL: POST /api/cuentas/{id}/movimientos
     */
    @PostMapping("/{id}/movimientos")
    public ResponseEntity<Void> registrarMovimiento(@PathVariable Integer id, @RequestBody MovimientoRequest request) {
        return cuentaRepository.findById(id)
                .map(cuenta -> {
                    if ("DEBITO".equalsIgnoreCase(request.getTipo())) {
                        cuenta.setSaldo(cuenta.getSaldo().subtract(request.getMonto()));
                    } else if ("CREDITO".equalsIgnoreCase(request.getTipo())) {
                        cuenta.setSaldo(cuenta.getSaldo().add(request.getMonto()));
                    }
                    // Guardamos el cambio en BD
                    cuentaRepository.save(cuenta);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}