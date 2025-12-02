package com.example.streetball_backend.User;

public class LoginRequest {
    private String name;
    private String password;
    private Double locationLat;
    private Double locationLng;

    // Constructors
    public LoginRequest() {
    }

    public LoginRequest(String name, String password, Double locationLat, Double locationLng) {
        this.name = name;
        this.password = password;
        this.locationLat = locationLat;
        this.locationLng = locationLng;
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

    public Double getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(Double locationLat) {
        this.locationLat = locationLat;
    }

    public Double getLocationLng() {
        return locationLng;
    }

    public void setLocationLng(Double locationLng) {
        this.locationLng = locationLng;
    }
}

