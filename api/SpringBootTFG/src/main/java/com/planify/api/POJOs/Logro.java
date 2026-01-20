package com.planify.api.POJOs;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "logro", schema = "PlanifyBD_composedhe")
public class Logro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idLogro", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idUser", nullable = false)
    private Usuario idUser;

    @Size(max = 30)
    @NotNull
    @Column(name = "nombre", nullable = false, length = 30)
    private String nombre;

    @NotNull
    @Column(name = "meta", nullable = false)
    private Integer meta;

    @NotNull
    @Lob
    @Column(name = "estado", nullable = false)
    private String estado;

    @Column(name = "fechaLim")
    private Instant fechaLim;

    @Column(name = "fechaCumpl")
    private Instant fechaCumpl;

    @ManyToMany
    private Set<Tarea> tareas = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuario getIdUser() {
        return idUser;
    }

    public void setIdUser(Usuario idUser) {
        this.idUser = idUser;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getMeta() {
        return meta;
    }

    public void setMeta(Integer meta) {
        this.meta = meta;
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

    public Instant getFechaCumpl() {
        return fechaCumpl;
    }

    public void setFechaCumpl(Instant fechaCumpl) {
        this.fechaCumpl = fechaCumpl;
    }

    public Set<Tarea> getTareas() {
        return tareas;
    }

    public void setTareas(Set<Tarea> tareas) {
        this.tareas = tareas;
    }

}