package com.planify.api.POJOs;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "calendario", schema = "PlanifyBD_composedhe")
public class Calendario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCal", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "fechaCreacion", nullable = false)
    private Instant fechaCreacion;

    @NotNull
    @Column(name = "codigo", nullable = false)
    private Integer codigo;

    @OneToMany(mappedBy = "idCal")
    private Set<RelUserCal> relUserCals = new LinkedHashSet<>();

    @OneToMany(mappedBy = "idCal")
    private Set<Tarea> tareas = new LinkedHashSet<>();

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

    public Instant getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Set<RelUserCal> getRelUserCals() {
        return relUserCals;
    }

    public void setRelUserCals(Set<RelUserCal> relUserCals) {
        this.relUserCals = relUserCals;
    }

    public Set<Tarea> getTareas() {
        return tareas;
    }

    public void setTareas(Set<Tarea> tareas) {
        this.tareas = tareas;
    }

}