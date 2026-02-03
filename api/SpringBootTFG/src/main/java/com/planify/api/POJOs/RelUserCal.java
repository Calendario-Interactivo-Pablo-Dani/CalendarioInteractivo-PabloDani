package com.planify.api.POJOs;

import com.planify.api.enums.RolUsuarioCalendario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "relUserCal")
public class RelUserCal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idRel", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "idUser", nullable = false)
    private Usuario idUser;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idCal", nullable = false)
    private Calendario idCal;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false)
    private RolUsuarioCalendario rol;

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

    public Calendario getIdCal() {
        return idCal;
    }

    public void setIdCal(Calendario idCal) {
        this.idCal = idCal;
    }

    public RolUsuarioCalendario getRol() {
        return rol;
    }

    public void setRol(RolUsuarioCalendario rol) {
        this.rol = rol;
    }

}