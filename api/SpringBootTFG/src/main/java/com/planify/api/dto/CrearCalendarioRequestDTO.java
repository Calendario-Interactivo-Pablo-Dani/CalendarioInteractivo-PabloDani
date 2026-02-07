package com.planify.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CrearCalendarioRequestDTO {
    @NotBlank
    private String nombre;


    public String getNombre() { return nombre; }

    public void setNombre(String nombre) { this.nombre = nombre; }
}
