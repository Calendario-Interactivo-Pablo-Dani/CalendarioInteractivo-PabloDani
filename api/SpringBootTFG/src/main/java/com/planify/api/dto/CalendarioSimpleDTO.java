package com.planify.api.dto;

import com.planify.api.enums.RolUsuarioCalendario;

public class CalendarioSimpleDTO {
    private Integer idCal;
    private String nombre;
    private Integer codigo;
    private RolUsuarioCalendario rol;

    public CalendarioSimpleDTO(Integer idCal, String nombre, Integer codigo, RolUsuarioCalendario rol) {
        this.idCal = idCal;
        this.nombre = nombre;
        this.codigo = codigo;
        this.rol = rol;
    }

    public Integer getIdCal() { return idCal; }
    public String getNombre() { return nombre; }
    public Integer getCodigo() { return codigo; }
    public RolUsuarioCalendario getRol() { return rol; }
}
