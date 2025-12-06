package com.cuentas.productos.cbs.service;

import com.cuentas.productos.cbs.entities.Cuenta;
import com.cuentas.productos.cbs.entities.TipoCuenta;
import com.cuentas.productos.cbs.model.CrearCuentaRequest;
import com.cuentas.productos.cbs.model.CuentaResponse;
import com.cuentas.productos.cbs.repository.CuentaRepository;
import com.cuentas.productos.cbs.repository.TipoCuentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class CuentaService {

    private final CuentaRepository cuentaRepository;
    private final TipoCuentaRepository tipoCuentaRepository;

    public CuentaService(CuentaRepository cuentaRepository,
                         TipoCuentaRepository tipoCuentaRepository) {
        this.cuentaRepository = cuentaRepository;
        this.tipoCuentaRepository = tipoCuentaRepository;
    }

    @Transactional
    public CuentaResponse crearCuenta(CrearCuentaRequest request) {

        TipoCuenta tipoCuenta = tipoCuentaRepository.findById(request.getTipoCuentaId())
                .orElseThrow(() -> new IllegalArgumentException("Tipo de cuenta no existe"));

        Cuenta cuenta = new Cuenta();
        cuenta.setClienteId(request.getClienteId());
        cuenta.setTipoCuenta(tipoCuenta);
        cuenta.setSucursalIdApertura(request.getSucursalIdApertura());
        cuenta.setNumeroCuenta(generarNumeroCuenta());
        cuenta.setSaldo(request.getSaldoInicial() != null ?
                request.getSaldoInicial() : BigDecimal.ZERO);
        cuenta.setFechaApertura(LocalDate.now());
        cuenta.setEstado("ACTIVA");

        Cuenta guardada = cuentaRepository.save(cuenta);
        return toResponse(guardada);
    }

    public CuentaResponse obtenerPorId(Integer id) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada"));
        return toResponse(cuenta);
    }

    public List<CuentaResponse> listarPorCliente(Integer clienteId) {
        return cuentaRepository.findByClienteId(clienteId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private String generarNumeroCuenta() {
        return String.valueOf(System.currentTimeMillis()).substring(3);
    }

    private CuentaResponse toResponse(Cuenta cuenta) {
        CuentaResponse dto = new CuentaResponse();
        dto.setCuentaId(cuenta.getId());
        dto.setNumeroCuenta(cuenta.getNumeroCuenta());
        dto.setClienteId(cuenta.getClienteId());
        dto.setTipoCuentaId(cuenta.getTipoCuenta().getId());
        dto.setSucursalIdApertura(cuenta.getSucursalIdApertura());
        dto.setSaldo(cuenta.getSaldo());
        dto.setFechaApertura(cuenta.getFechaApertura());
        dto.setEstado(cuenta.getEstado());
        return dto;
    }
}
