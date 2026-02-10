package com.example.planify.data.POJOs;

public class Miembro {
    private String username;

    public Miembro(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public Miembro() {
    }
    public void setUsername(String username) {
        this.username = username;
    }
}
