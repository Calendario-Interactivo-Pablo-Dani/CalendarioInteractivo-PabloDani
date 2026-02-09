package com.planify.api.dto;

import com.planify.api.enums.ColorTarea;
import com.planify.api.enums.EstadoTarea;
import com.planify.api.enums.TipoTarea;

import java.time.Instant;
import java.time.LocalDateTime;

public class TareaNuevaResponseDTO {

    Integer idTarea;
    String nombre;
    TipoTarea tipo;
    EstadoTarea estado;
    LocalDateTime fechaLim;
    ColorTarea color;
    public TareaNuevaResponseDTO(Integer idTarea, String nombre, TipoTarea tipo, EstadoTarea estado,  LocalDateTime fechaLim, ColorTarea color) {
        this.idTarea = idTarea;
        this.nombre = nombre;
        this.tipo = tipo;
        this.estado = estado;
        this.fechaLim = fechaLim;
        this.color = color;
    }
    public Integer getIdTarea() { return idTarea; }
    public String getNombre() { return nombre; }
    public TipoTarea getTipo() { return tipo; }
    public EstadoTarea getEstado() { return estado; }
    public LocalDateTime getFechaLim() { return fechaLim; }
    public ColorTarea getColor() { return color; }
}
