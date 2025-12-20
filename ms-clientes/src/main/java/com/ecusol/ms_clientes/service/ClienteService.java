package com.ecusol.ms_clientes.service;

import com.ecusol.ms_clientes.dto.RegistroClientePersonaDTO;
import com.ecusol.ms_clientes.exception.ClienteCreacionException;
import com.ecusol.ms_clientes.exception.ClienteYaRegistradoException;
import com.ecusol.ms_clientes.mapper.PersonaMapper;
import com.ecusol.ms_clientes.model.Persona;
import com.ecusol.ms_clientes.repository.PersonaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteService {

    private static final Logger log = LoggerFactory.getLogger(ClienteService.class);

    private final PersonaRepository personaRepository;
    private final PersonaMapper personaMapper;

    public ClienteService(PersonaRepository personaRepository, PersonaMapper personaMapper) {
        this.personaRepository = personaRepository;
        this.personaMapper = personaMapper;
    }

    @Transactional
    public Integer crearClientePersona(RegistroClientePersonaDTO dto) {

        final String cedula = dto.getCedula();

        log.info("Intentando crear cliente persona con identificación={}", cedula);

        if (personaRepository.findByNumeroIdentificacion(cedula).isPresent()) {
            log.warn("Creación rechazada: identificación duplicada={}", cedula);
            throw new ClienteYaRegistradoException(cedula);
        }

        try {
            Persona persona = personaMapper.toEntity(dto);

            Persona personaGuardada = personaRepository.save(persona);

            log.info("Cliente persona creado correctamente. clienteId={}, identificación={}",
                    personaGuardada.getClienteId(), cedula);

            return personaGuardada.getClienteId();

        } catch (ClienteYaRegistradoException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado creando cliente persona. identificación={}", cedula, e);
            throw new ClienteCreacionException("No se pudo crear el cliente persona", e);
        }
    }
}