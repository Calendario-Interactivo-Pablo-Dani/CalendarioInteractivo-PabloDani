package com.example.planify.data.dto;

public class RegisterRequestDTO {

    private String nombre;
    private String username;
    private String email;
    private String password;
    private String telefono;
    public RegisterRequestDTO(String nombre, String username, String email, String password, String telefono) {
        this.nombre = nombre;
        this.username = username;
        this.email = email;
        this.password = password;
        this.telefono = telefono;
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
    public String getPassword() {
        return password;
    }
    public String getTelefono() {
        return telefono;
    }


}
