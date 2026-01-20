package com.planify.api.POJOs;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tarea")
public class Tarea {
    @Id
    @Column(name = "idTarea")
    private Integer idTarea;

    @Column(name = "idCal")
    private Integer idCal;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "fecha")
    private LocalDateTime fecha;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "estado")
    private String estado;

    @Column(name = "fechaLim")
    private LocalDateTime fechaLim;

    public Integer getIdTarea() {
        return this.idTarea;
    }

    public void setIdTarea(Integer idTarea) {
        this.idTarea = idTarea;
    }

    public Integer getIdCal() {
        return this.idCal;
    }

    public void setIdCal(Integer idCal) {
        this.idCal = idCal;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDateTime getFecha() {
        return this.fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getTipo() {
        return this.tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEstado() {
        return this.estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaLim() {
        return this.fechaLim;
    }

    public void setFechaLim(LocalDateTime fechaLim) {
        this.fechaLim = fechaLim;
    }
}
