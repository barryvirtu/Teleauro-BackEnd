package com.teleauro.authentication.dto;

public class LoginResponse {
    private String message;
    private String token;
    private long jwtDuration;
    private long totalDuration;

    public LoginResponse(String message, String token, long jwtDuration, long totalDuration) {
        this.message = message;
        this.token = token;
        this.jwtDuration = jwtDuration;
        this.totalDuration = totalDuration;
    }

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }

    public long getJwtDuration() {
        return jwtDuration;
    }

    public long getTotalDuration() {
        return totalDuration;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setJwtDuration(long jwtDuration) {
        this.jwtDuration = jwtDuration;
    }

    public void setTotalDuration(long totalDuration) {
        this.totalDuration = totalDuration;
    }
}
