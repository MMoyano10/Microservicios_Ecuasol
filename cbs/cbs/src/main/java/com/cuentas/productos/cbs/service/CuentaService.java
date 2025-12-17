package com.cuentas.productos.cbs.service;

import com.cuentas.productos.cbs.dto.CrearCuentaRequest;
import com.cuentas.productos.cbs.dto.CuentaResponse;
import com.cuentas.productos.cbs.exception.BusinessException;
import com.cuentas.productos.cbs.exception.ResourceNotFoundException;
import com.cuentas.productos.cbs.mapper.CuentaMapper;
import com.cuentas.productos.cbs.model.Cuenta;
import com.cuentas.productos.cbs.model.TipoCuenta;
import com.cuentas.productos.cbs.repository.CuentaRepository;
import com.cuentas.productos.cbs.repository.TipoCuentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class CuentaService {

    private final CuentaRepository cuentaRepo;
    private final TipoCuentaRepository tipoCuentaRepo;

    public CuentaService(CuentaRepository cuentaRepo, TipoCuentaRepository tipoCuentaRepo) {
        this.cuentaRepo = cuentaRepo;
        this.tipoCuentaRepo = tipoCuentaRepo;
    }

    @Transactional
    public CuentaResponse crear(CrearCuentaRequest req) {

        if (req.getSaldoInicial() != null && req.getSaldoInicial().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("El saldo inicial no puede ser negativo");
        }

        TipoCuenta tipo = tipoCuentaRepo.findById(req.getTipoCuentaId())
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de cuenta no existe: " + req.getTipoCuentaId()));

        Cuenta cuenta = new Cuenta();
        cuenta.setClienteId(req.getClienteId());
        cuenta.setTipoCuenta(tipo);
        cuenta.setSucursalIdApertura(req.getSucursalIdApertura());
        cuenta.setNumeroCuenta(generarNumeroCuenta());
        cuenta.setSaldo(req.getSaldoInicial() != null ? req.getSaldoInicial() : BigDecimal.ZERO);
        cuenta.setFechaApertura(LocalDate.now());
        cuenta.setEstado("ACTIVA");

        return CuentaMapper.toResponse(cuentaRepo.save(cuenta));
    }

    public CuentaResponse obtener(Integer id) {
        Cuenta cuenta = cuentaRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada: " + id));
        return CuentaMapper.toResponse(cuenta);
    }

    public List<CuentaResponse> listarPorCliente(Integer clienteId) {
        return cuentaRepo.findByClienteId(clienteId).stream()
                .map(CuentaMapper::toResponse)
                .toList();
    }

    private String generarNumeroCuenta() {
        return String.valueOf(System.currentTimeMillis()).substring(3);
    }
}
