package com.planify.api.dto;

import com.planify.api.enums.ColorTarea;
import com.planify.api.enums.EstadoTarea;
import com.planify.api.enums.TipoTarea;

import java.time.Instant;

public class TareaNuevaResponseDTO {

    String nombre;
    TipoTarea tipo;
    EstadoTarea estado;
    Instant fechaLim;
    ColorTarea color;
    public TareaNuevaResponseDTO(String nombre, TipoTarea tipo, EstadoTarea estado,  Instant fechaLim, ColorTarea color) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.estado = estado;
        this.fechaLim = fechaLim;
        this.color = color;
    }
    public String getNombre() { return nombre; }
    public TipoTarea getTipo() { return tipo; }
    public EstadoTarea getEstado() { return estado; }
    public Instant getFechaLim() { return fechaLim; }
    public ColorTarea getColor() { return color; }
}
