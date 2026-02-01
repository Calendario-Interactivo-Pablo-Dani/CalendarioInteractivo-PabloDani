package com.planify.api.dto;

public class CalendarioSimpleDTO {
    private Integer idCal;
    private String nombre;
    private Integer codigo;
    private String rol;

    public CalendarioSimpleDTO(Integer idCal, String nombre, Integer codigo, String rol) {
        this.idCal = idCal;
        this.nombre = nombre;
        this.codigo = codigo;
        this.rol = rol;
    }

    public Integer getIdCal() { return idCal; }
    public String getNombre() { return nombre; }
    public Integer getCodigo() { return codigo; }
    public String getRol() { return rol; }
}
