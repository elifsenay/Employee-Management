package com.example.employeemanagement.model;

public record AuthenticationResponse(String jwt) {
    public String getJwt() {
        return jwt;
    }
}
