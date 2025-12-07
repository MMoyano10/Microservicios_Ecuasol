package com.ecusol.ms_clientes.service;

import com.ecusol.ms_clientes.dto.RegistroClientePersonaDTO;
import com.ecusol.ms_clientes.model.Persona;
import com.ecusol.ms_clientes.repository.PersonaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class ClienteService {

    private final PersonaRepository personaRepository;

    // ✅ Constructor para inyectar el repositorio
    public ClienteService(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    @Transactional
    public Integer crearClientePersona(RegistroClientePersonaDTO dto) {

        // Validar que la cédula no exista ya
        if (personaRepository.findByNumeroIdentificacion(dto.getCedula()).isPresent()) {
            throw new IllegalArgumentException("La cédula ya se encuentra registrada en Clientes");
        }

        Persona persona = new Persona();
        persona.setTipoCliente("P");
        persona.setEstado("ACTIVO");
        persona.setFechaRegistro(LocalDate.now());
        persona.setNombres(dto.getNombres());
        persona.setApellidos(dto.getApellidos());
        persona.setNumeroIdentificacion(dto.getCedula());
        persona.setTipoIdentificacion("CEDULA");
        persona.setDireccion(dto.getDireccion());
        persona.setFechaNacimiento(
                dto.getFechaNacimiento() != null ? dto.getFechaNacimiento() : LocalDate.now()
        );

        Persona personaGuardada = personaRepository.save(persona);
        return personaGuardada.getClienteId();
    }
}
