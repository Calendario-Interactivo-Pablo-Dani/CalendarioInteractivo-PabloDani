package com.planify.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CrearCalendarioRequestDTO {
    @NotBlank
    private String nombre;

    @NotNull
    private Integer codigo;

    public String getNombre() { return nombre; }
    public Integer getCodigo() { return codigo; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setCodigo(Integer codigo) { this.codigo = codigo; }
}
