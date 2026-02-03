package com.planify.api.POJOs;

import com.planify.api.enums.EstadoLogro;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "logro")
public class Logro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idLogro", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idUser", nullable = false)
    private Usuario usuario;

    @Column(name = "nombre", nullable = false, length = 30)
    private String nombre;

    @Column(name = "meta", nullable = false)
    private Integer meta;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoLogro estado;

    @Column(name = "fechaLim")
    private Instant fechaLim;

    @Column(name = "fechaCumpl")
    private Instant fechaCumpl;

    /* ========= RELACIÓN CON TAREA (VÍA TABLA INTERMEDIA) ========= */

    @OneToMany(
            mappedBy = "idLogro",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<LogroTarea> logroTareas = new HashSet<>();

    /* ================= GETTERS / SETTERS ================= */

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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

    public EstadoLogro getEstado() {
        return estado;
    }

    public void setEstado(EstadoLogro estado) {
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

    public Set<LogroTarea> getLogroTareas() {
        return logroTareas;
    }

    public void setLogroTareas(Set<LogroTarea> logroTareas) {
        this.logroTareas = logroTareas;
    }
}