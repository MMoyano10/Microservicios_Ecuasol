package com.ecusol.ms_transacciones.dto.hermes;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SolicitudSwitchWrapper {
    
    @JsonProperty("Transaccion")
    private TransaccionSwitchDTO transaccion;
}