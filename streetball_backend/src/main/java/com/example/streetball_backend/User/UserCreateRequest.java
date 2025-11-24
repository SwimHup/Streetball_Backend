package com.example.streetball_backend.User;

public class UserCreateRequest {
    private String name;
    private Double locationLat;
    private Double locationLng;
    private Boolean hasBall;

    // Constructors
    public UserCreateRequest() {
    }

    public UserCreateRequest(String name, Double locationLat, Double locationLng, Boolean hasBall) {
        this.name = name;
        this.locationLat = locationLat;
        this.locationLng = locationLng;
        this.hasBall = hasBall;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Boolean getHasBall() {
        return hasBall;
    }

    public void setHasBall(Boolean hasBall) {
        this.hasBall = hasBall;
    }
}

