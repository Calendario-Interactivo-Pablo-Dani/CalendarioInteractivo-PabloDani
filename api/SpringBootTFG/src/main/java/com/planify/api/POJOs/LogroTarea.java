package com.planify.api.POJOs;

import jakarta.persistence.*;

@Entity
@Table(name = "logroTarea", schema = "PlanifyBD_composedhe")
public class LogroTarea {
    @EmbeddedId
    private LogroTareaId id;

    @MapsId("idLogro")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idLogro", nullable = false)
    private Logro idLogro;

    @MapsId("idTarea")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idTarea", nullable = false)
    private Tarea idTarea;

    public LogroTareaId getId() {
        return id;
    }

    public void setId(LogroTareaId id) {
        this.id = id;
    }

    public Logro getIdLogro() {
        return idLogro;
    }

    public void setIdLogro(Logro idLogro) {
        this.idLogro = idLogro;
    }

    public Tarea getIdTarea() {
        return idTarea;
    }

    public void setIdTarea(Tarea idTarea) {
        this.idTarea = idTarea;
    }

}