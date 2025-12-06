package com.cuentas.productos.cbs.service;

import com.cuentas.productos.cbs.entities.TasaInteresHistorico;
import com.cuentas.productos.cbs.entities.TipoCuenta;
import com.cuentas.productos.cbs.repository.TasaInteresHistoricoRepository;
import com.cuentas.productos.cbs.repository.TipoCuentaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TasaInteresService {

    private final TasaInteresHistoricoRepository repo;
    private final TipoCuentaRepository tipoCuentaRepo;

    public TasaInteresService(TasaInteresHistoricoRepository repo,
                              TipoCuentaRepository tipoCuentaRepo) {
        this.repo = repo;
        this.tipoCuentaRepo = tipoCuentaRepo;
    }

    public List<TasaInteresHistorico> listarTodas() {
        return repo.findAll();
    }

    public List<TasaInteresHistorico> listarPorTipoCuenta(Integer tipoCuentaId) {
        return repo.findByTipoCuentaIdOrderByFechaInicioVigenciaDesc(tipoCuentaId);
    }

    public TasaInteresHistorico obtener(Integer id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tasa no encontrada"));
    }

    public TasaInteresHistorico crear(Integer tipoCuentaId, TasaInteresHistorico tasa) {
        TipoCuenta tipo = tipoCuentaRepo.findById(tipoCuentaId)
                .orElseThrow(() -> new IllegalArgumentException("Tipo de cuenta no encontrado"));
        tasa.setId(null);
        tasa.setTipoCuenta(tipo);
        return repo.save(tasa);
    }

    public void eliminar(Integer id) {
        repo.deleteById(id);
    }
}
