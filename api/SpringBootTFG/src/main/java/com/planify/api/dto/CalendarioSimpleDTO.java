package com.planify.api.dto;

import com.planify.api.enums.RolUsuarioCalendario;

public class CalendarioSimpleDTO {
    private Integer idCal;
    private String nombre;
    private String codigo;
    private RolUsuarioCalendario rol;

    public CalendarioSimpleDTO(Integer idCal, String nombre, String codigo, RolUsuarioCalendario rol) {
        this.idCal = idCal;
        this.nombre = nombre;
        this.codigo = codigo;
        this.rol = rol;
    }

    public Integer getIdCal() { return idCal; }
    public String getNombre() { return nombre; }
    public String getCodigo() { return codigo; }
    public RolUsuarioCalendario getRol() { return rol; }
}
