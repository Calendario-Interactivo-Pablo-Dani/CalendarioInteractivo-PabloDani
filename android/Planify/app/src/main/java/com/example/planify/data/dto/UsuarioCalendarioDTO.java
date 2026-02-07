package com.example.planify.data.dto;

public class UsuarioCalendarioDTO {
    private int idUser;
    private String nombre;
    private String email;
    private String rol;

    private String username;

    public int getIdUser() { return idUser; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getRol() { return rol; }

    public String getUsername(){return username;}
}
