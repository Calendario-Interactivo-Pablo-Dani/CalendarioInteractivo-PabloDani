package com.planify.api.POJOs;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class LogroTareaId implements Serializable {
    private static final long serialVersionUID = -5566612083116559556L;
    @NotNull
    @Column(name = "idLogro", nullable = false)
    private Integer idLogro;

    @NotNull
    @Column(name = "idTarea", nullable = false)
    private Integer idTarea;

    public Integer getIdLogro() {
        return idLogro;
    }

    public void setIdLogro(Integer idLogro) {
        this.idLogro = idLogro;
    }

    public Integer getIdTarea() {
        return idTarea;
    }

    public void setIdTarea(Integer idTarea) {
        this.idTarea = idTarea;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        LogroTareaId entity = (LogroTareaId) o;
        return Objects.equals(this.idLogro, entity.idLogro) &&
                Objects.equals(this.idTarea, entity.idTarea);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idLogro, idTarea);
    }

}