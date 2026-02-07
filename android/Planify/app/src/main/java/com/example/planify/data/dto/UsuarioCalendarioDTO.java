package com.example.planify.data.dto;

public class UsuarioCalendarioDTO {
    private int idUser;
    private String nombre;   // lo usaremos como "username" en la UI
    private String email;
    private String rol;

    public int getIdUser() { return idUser; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getRol() { return rol; }
}
