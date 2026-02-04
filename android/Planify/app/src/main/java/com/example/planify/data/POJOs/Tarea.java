package com.example.planify.data.POJOs;

import java.time.Instant;
import java.util.Set;

public class Tarea {

    private Integer id;
    private Calendario idCal;
    private String nombre;
    private Instant fecha;
    private String tipo;
    private String estado;
    private Instant fechaLim;
    private Set<Logro> logros;

    private String color;



    // Constructor vacío (OBLIGATORIO para Retrofit/Gson)
    public Tarea() {
    }

    // Getters y setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Calendario getIdCal() {
        return idCal;
    }

    public void setIdCal(Calendario idCal) {
        this.idCal = idCal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Instant getFecha() {
        return fecha;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
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

    public Instant getFechaLim() {
        return fechaLim;
    }

    public void setFechaLim(Instant fechaLim) {
        this.fechaLim = fechaLim;
    }

    public Set<Logro> getLogros() {
        return logros;
    }

    public void setLogros(Set<Logro> logros) {
        this.logros = logros;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}


