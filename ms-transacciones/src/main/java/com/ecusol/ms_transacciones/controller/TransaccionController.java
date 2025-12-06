package com.ecusol.ms_transacciones.controller;

import com.ecusol.ms_transacciones.dto.SolicitudTransferenciaRequest;
import com.ecusol.ms_transacciones.dto.hermes.SwitchTransferRequest;
import com.ecusol.ms_transacciones.model.Transaccion;
import com.ecusol.ms_transacciones.service.TransaccionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TransaccionController {

    private final TransaccionService transaccionService;

    // 1. Endpoint para enviar dinero (Salida)
    // Usado por: Banca Web / Móvil
    @PostMapping("/v1/transacciones/enviar")
    public ResponseEntity<Transaccion> enviarTransferencia(@RequestBody SolicitudTransferenciaRequest request) {
        
        Transaccion nuevaTransaccion = transaccionService.realizarTransferencia(
                request.getCuentaOrigenId(),
                request.getMonto(),
                request.getCuentaDestino(),
                request.getBancoDestinoBic()
        );

        // Retornamos 200 OK con la transacción creada
        return ResponseEntity.ok(nuevaTransaccion);
    }

    // 2. Webhook para recibir dinero (Entrada)
    // Usado por: Switch Hermes (RF-01, Sección 4.3 del PDF)
    @PostMapping("/incoming/credit")
    public ResponseEntity<Void> recibirTransferencia(@RequestBody SwitchTransferRequest webhookRequest) {
        
        // Llamamos al servicio para procesar el abono
        transaccionService.procesarRecepcion(webhookRequest);

        // Retornamos 200 OK para confirmar al Switch que aceptamos el dinero [cite: 505-506]
        return ResponseEntity.ok().build();
    }
}