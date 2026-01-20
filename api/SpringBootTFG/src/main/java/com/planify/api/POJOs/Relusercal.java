package com.planify.api.POJOs;

import jakarta.persistence.*;

@Entity
@Table(name = "relusercal")
public class Relusercal {
    @Id
    @Column(name = "idRel")
    private Integer idRel;

    @Column(name = "idUser")
    private Integer idUser;

    @Column(name = "idCal")
    private Integer idCal;

    @Column(name = "rol")
    private String rol;

    public Integer getIdRel() {
        return this.idRel;
    }

    public void setIdRel(Integer idRel) {
        this.idRel = idRel;
    }

    public Integer getIdUser() {
        return this.idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public Integer getIdCal() {
        return this.idCal;
    }

    public void setIdCal(Integer idCal) {
        this.idCal = idCal;
    }

    public String getRol() {
        return this.rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
