package com.teleauro.authentication.dto;


public class LoginRequest {
    private String username;
    private String password; // <-- changed from passwordHash

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
}
