package com.cuentas.productos.cbs.service;

import com.cuentas.productos.cbs.entities.TipoCuenta;
import com.cuentas.productos.cbs.repository.TipoCuentaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoCuentaService {

    private final TipoCuentaRepository repo;

    public TipoCuentaService(TipoCuentaRepository repo) {
        this.repo = repo;
    }

    public List<TipoCuenta> listar() {
        return repo.findAll();
    }

    public TipoCuenta obtener(Integer id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tipo de cuenta no encontrado"));
    }

    public TipoCuenta crear(TipoCuenta tipo) {
        tipo.setId(null);
        return repo.save(tipo);
    }

    public TipoCuenta actualizar(Integer id, TipoCuenta tipo) {
        if (!repo.existsById(id)) {
            throw new IllegalArgumentException("Tipo de cuenta no encontrado");
        }
        tipo.setId(id);
        return repo.save(tipo);
    }

    public void eliminar(Integer id) {
        repo.deleteById(id);
    }
}
