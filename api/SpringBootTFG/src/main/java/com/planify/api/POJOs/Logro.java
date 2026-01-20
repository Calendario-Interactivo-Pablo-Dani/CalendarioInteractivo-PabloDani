package com.planify.api.POJOs;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "logro")
public class Logro {
    @Id
    @Column(name = "idLogro")
    private Integer idLogro;

    @Column(name = "idUser")
    private Integer idUser;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "meta")
    private Integer meta;

    @Column(name = "estado")
    private String estado;

    @Column(name = "fechaLim")
    private LocalDateTime fechaLim;

    @Column(name = "fechaCumpl")
    private LocalDateTime fechaCumpl;

    public Integer getIdLogro() {
        return this.idLogro;
    }

    public void setIdLogro(Integer idLogro) {
        this.idLogro = idLogro;
    }

    public Integer getIdUser() {
        return this.idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getMeta() {
        return this.meta;
    }

    public void setMeta(Integer meta) {
        this.meta = meta;
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

    public LocalDateTime getFechaCumpl() {
        return this.fechaCumpl;
    }

    public void setFechaCumpl(LocalDateTime fechaCumpl) {
        this.fechaCumpl = fechaCumpl;
    }
}
