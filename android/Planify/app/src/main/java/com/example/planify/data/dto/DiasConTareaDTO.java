package com.example.planify.data.dto;

import java.time.LocalDate;
import java.util.List;

public class DiasConTareaDTO {
    private LocalDate fecha;
    private List<String> colores;

    // Constructores, getters y setters
    public LocalDate getFecha() {
        return fecha;
    }
    public List<String> getColores() {
        return colores;
    }
}
