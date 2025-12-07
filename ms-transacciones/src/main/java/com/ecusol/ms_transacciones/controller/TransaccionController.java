package com.ecusol.ms_transacciones.controller;

import com.ecusol.ms_transacciones.dto.SolicitudTransferenciaRequest;
import com.ecusol.ms_transacciones.dto.hermes.SolicitudSwitchWrapper;
import com.ecusol.ms_transacciones.model.Transaccion;
import com.ecusol.ms_transacciones.service.TransaccionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transacciones")
@RequiredArgsConstructor
public class TransaccionController {

    private final TransaccionService transaccionService;

    // Tu cliente envía dinero (Débito)
    @PostMapping("/enviar")
    public ResponseEntity<Transaccion> enviarTransferencia(@RequestBody SolicitudTransferenciaRequest request) {
        return ResponseEntity.ok(transaccionService.realizarTransferencia(request));
    }

    // El Switch te envía dinero (Crédito / Webhook)
    // Este endpoint recibe el JSON estricto del PDF
    @PostMapping("/recepcion")
    public ResponseEntity<Transaccion> recibirDinero(@RequestBody SolicitudSwitchWrapper request) {
        return ResponseEntity.ok(transaccionService.recibirTransferencia(request));
    }
}