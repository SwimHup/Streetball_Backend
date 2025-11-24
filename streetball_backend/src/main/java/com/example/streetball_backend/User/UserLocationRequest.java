package com.example.streetball_backend.User;

public class UserLocationRequest {
    private Double locationLat;
    private Double locationLng;

    // Constructors
    public UserLocationRequest() {
    }

    public UserLocationRequest(Double locationLat, Double locationLng) {
        this.locationLat = locationLat;
        this.locationLng = locationLng;
    }

    // Getters and Setters
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

