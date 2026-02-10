package com.example.planify.data.dto;

import java.time.LocalDate;
import java.util.List;

public class DiasConTareaDTO {
    private String fecha;
    private List<String> colores;

    // Constructores, getters y setters
    public String getFecha() {
        return fecha;
    }
    public List<String> getColores() {
        return colores;
    }
}
