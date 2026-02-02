package com.example.planify.data.dto;

public class CalendarioRequestDTO {
    private String nombre;
    private int codigo;

    public CalendarioRequestDTO(String nombre, int codigo) {
        this.nombre = nombre;
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCodigo() {
        return codigo;
    }
}
