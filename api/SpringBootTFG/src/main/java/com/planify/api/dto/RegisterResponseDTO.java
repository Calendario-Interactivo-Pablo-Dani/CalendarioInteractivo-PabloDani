package com.planify.api.dto;

public class RegisterResponseDTO {
    private Integer id;
    private String nombre;
    private String username;
    private String email;
    private String telefono;
    private String mensaje;

    public RegisterResponseDTO(Integer id, String nombre, String username, String email, String telefono, String mensaje) {
        this.id = id;
        this.nombre = nombre;
        this.username = username;
        this.email = email;
        this.telefono = telefono;
        this.mensaje = mensaje;
    }
    public Integer getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getMensaje() {
        return mensaje;
    }

}
