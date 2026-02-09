package com.example.planify.data.dto;

public class TareaNuevaRequestDTO {

    private Integer idCal;
    private String nombre;
    private String tipo;
    private String estado;
    private String fechaLim;   // "18:30"
    private String color;

    // getters y setters


    public Integer getIdCal() {
        return idCal;
    }

    public void setIdCal(Integer idCal) {
        this.idCal = idCal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFechaLim() {
        return fechaLim;
    }

    public void setFechaLim(String fechaLim) {
        this.fechaLim = fechaLim;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}

