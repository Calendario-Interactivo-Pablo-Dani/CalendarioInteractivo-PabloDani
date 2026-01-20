package com.planify.api.POJOs;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "calendario")
public class Calendario {
    @Id
    @Column(name = "idCal")
    private Integer idCal;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "fechaCreacion")
    private LocalDateTime fechaCreacion;

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

    public LocalDateTime getFechaCreacion() {
        return this.fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
