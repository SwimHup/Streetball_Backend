package com.example.streetball_backend.User;

public class SignupRequest {
    private String name;
    private String password;
    private Boolean hasBall;

    // Constructors
    public SignupRequest() {
    }

    public SignupRequest(String name, String password, Boolean hasBall) {
        this.name = name;
        this.password = password;
        this.hasBall = hasBall;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getHasBall() {
        return hasBall;
    }

    public void setHasBall(Boolean hasBall) {
        this.hasBall = hasBall;
    }
}

