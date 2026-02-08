package com.planify.api.dto;

import com.planify.api.enums.ColorTarea;
import com.planify.api.enums.EstadoTarea;
import com.planify.api.enums.TipoTarea;

import java.time.Instant;

public class TareaNuevaRequestDTO {
    Integer idCal;
    String nombre;
    TipoTarea tipo;
    EstadoTarea estado;
    Instant fechaLim;
    ColorTarea color;

    public Integer getIdCal() {return idCal;}
    public void setIdCal(Integer idCal) {this.idCal = idCal;}
    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public TipoTarea getTipo() {return tipo;}
    public void setTipo(TipoTarea tipo) {this.tipo = tipo;}
    public EstadoTarea getEstado() {return estado;}
    public void setEstado(EstadoTarea estado) {this.estado = estado;}
    public Instant getFechaLim() {return fechaLim;}
    public void setFechaLim(Instant fechaLim) {this.fechaLim = fechaLim;}
    public ColorTarea getColor() {return color;}
    public void setColor(ColorTarea color) {this.color = color;}
}
