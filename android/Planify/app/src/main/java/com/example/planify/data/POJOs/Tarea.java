package com.example.planify.data.POJOs;

import com.google.gson.annotations.SerializedName;

import java.time.Instant;
import java.util.Set;

public class Tarea {

    @SerializedName("idTarea")
    private Integer id;
    private String nombre;
    private String tipo;
    private String estado;
    private String fechaLim; // "yyyy-MM-ddTHH:mm"



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


