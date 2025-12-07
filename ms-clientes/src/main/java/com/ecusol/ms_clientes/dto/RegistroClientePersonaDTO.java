package com.ecusol.ms_clientes.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RegistroClientePersonaDTO {

    private String nombres;
    private String apellidos;
    private String cedula;
    private String direccion;
    private LocalDate fechaNacimiento;
}
