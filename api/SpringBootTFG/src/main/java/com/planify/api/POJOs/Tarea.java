package com.planify.api.POJOs;

import com.planify.api.enums.ColorTarea;
import com.planify.api.enums.EstadoTarea;
import com.planify.api.enums.TipoTarea;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "tarea")
public class Tarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idTarea", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idCal", nullable = false)
    private Calendario calendario;

    @Column(name = "nombre", nullable = false, length = 30)
    private String nombre;

    @Column(name = "fecha", nullable = false)
    private Instant fecha;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoTarea tipo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoTarea estado;

    @Column(name = "fechaLim")
    private Instant fechaLim;

    @Enumerated(EnumType.STRING)
    @Column(name = "color", nullable = false)
    private ColorTarea color;

    /* ========= RELACIÓN CON LOGRO (VÍA TABLA INTERMEDIA) ========= */

    @OneToMany(
            mappedBy = "idTarea",
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

    public Calendario getCalendario() {
        return calendario;
    }

    public void setCalendario(Calendario calendario) {
        this.calendario = calendario;
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

    public TipoTarea getTipo() {
        return tipo;
    }

    public void setTipo(TipoTarea tipo) {
        this.tipo = tipo;
    }

    public EstadoTarea getEstado() {
        return estado;
    }

    public void setEstado(EstadoTarea estado) {
        this.estado = estado;
    }

    public Instant getFechaLim() {
        return fechaLim;
    }

    public void setFechaLim(Instant fechaLim) {
        this.fechaLim = fechaLim;
    }

    public ColorTarea getColor() {
        return color;
    }

    public void setColor(ColorTarea color) {
        this.color = color;
    }

    public Set<LogroTarea> getLogroTareas() {
        return logroTareas;
    }

    public void setLogroTareas(Set<LogroTarea> logroTareas) {
        this.logroTareas = logroTareas;
    }
}