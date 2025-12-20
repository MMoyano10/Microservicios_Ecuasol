package com.ecusol.ms_clientes.mapper;

import com.ecusol.ms_clientes.dto.RegistroClientePersonaDTO;
import com.ecusol.ms_clientes.model.Persona;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = java.time.LocalDate.class)
public interface PersonaMapper {

    @Mapping(target = "tipoCliente", constant = "P")
    @Mapping(target = "estado", constant = "ACTIVO")
    @Mapping(target = "fechaRegistro", expression = "java(LocalDate.now())")
    @Mapping(target = "numeroIdentificacion", source = "cedula")
    @Mapping(target = "tipoIdentificacion", constant = "CEDULA")
    Persona toEntity(RegistroClientePersonaDTO dto);
}
