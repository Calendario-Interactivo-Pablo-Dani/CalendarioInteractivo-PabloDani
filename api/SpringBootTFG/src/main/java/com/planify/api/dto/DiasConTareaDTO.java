package com.planify.api.dto;

import java.time.LocalDate;
import java.util.List;

public class DiasConTareaDTO {
    private LocalDate fecha;
    private List<String> colores;

    public DiasConTareaDTO(LocalDate fecha, List<String> colores) {
        this.fecha = fecha;
        this.colores = colores;
    }
    public LocalDate getFecha() {
        return fecha;
    }
    public List<String> getColores() {
        return colores;
    }
}
