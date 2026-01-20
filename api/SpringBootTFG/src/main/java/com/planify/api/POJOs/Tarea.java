package com.planify.api.POJOs;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "tarea", schema = "PlanifyBD_composedhe")
public class Tarea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idTarea", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idCal", nullable = false)
    private Calendario idCal;

    @Size(max = 30)
    @NotNull
    @Column(name = "nombre", nullable = false, length = 30)
    private String nombre;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "fecha", nullable = false)
    private Instant fecha;

    @NotNull
    @Lob
    @Column(name = "tipo", nullable = false)
    private String tipo;

    @Lob
    @Column(name = "estado")
    private String estado;

    @Column(name = "fechaLim")
    private Instant fechaLim;

    @ManyToMany(mappedBy = "tareas")
    private Set<Logro> logroes = new LinkedHashSet<>();

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

    public Set<Logro> getLogroes() {
        return logroes;
    }

    public void setLogroes(Set<Logro> logroes) {
        this.logroes = logroes;
    }

}