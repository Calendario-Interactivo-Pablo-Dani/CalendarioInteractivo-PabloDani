package com.planify.api.dto;

import jakarta.validation.constraints.NotNull;

public class UsuarioCalendarioDTO {
    @NotNull
    private Integer idUser;
    @NotNull
    private String nombre;
    @NotNull
    private String email;
    @NotNull
    private String rol;
    @NotNull
    private String username;

    public UsuarioCalendarioDTO(Integer idUser, String nombre, String email, String rol, String username) {
        this.idUser = idUser;
        this.nombre = nombre;
        this.email = email;
        this.rol = rol;
        this.username = username;
    }

    public Integer getIdUser() { return idUser; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getRol() { return rol; }
    public String getUsername() { return username; }


    public void setIdUser(Integer idUser) { this.idUser = idUser; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setEmail(String email) { this.email = email; }
    public void setRol(String rol) { this.rol = rol; }
    public void setUsername(String username) { this.username = username; }
}
