package com.planify.api.POJOs;

import jakarta.persistence.*;

@Entity
@Table(name = "logrotarea")
public class Logrotarea {
    @Id
    @Column(name = "idLogroTarea")
    private Integer idLogroTarea;

    @Column(name = "idLogro")
    private Integer idLogro;

    @Column(name = "idTarea")
    private Integer idTarea;

    public Integer getIdLogroTarea() {
        return this.idLogroTarea;
    }

    public void setIdLogroTarea(Integer idLogroTarea) {
        this.idLogroTarea = idLogroTarea;
    }

    public Integer getIdLogro() {
        return this.idLogro;
    }

    public void setIdLogro(Integer idLogro) {
        this.idLogro = idLogro;
    }

    public Integer getIdTarea() {
        return this.idTarea;
    }

    public void setIdTarea(Integer idTarea) {
        this.idTarea = idTarea;
    }
}
