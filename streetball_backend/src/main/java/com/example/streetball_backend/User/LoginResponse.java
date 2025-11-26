package com.example.streetball_backend.User;

import java.time.LocalDateTime;

public class LoginResponse {
    private Integer userId;
    private String name;
    private Double locationLat;
    private Double locationLng;
    private Boolean hasBall;
    private LocalDateTime createdAt;
    private String message;

    // Constructors
    public LoginResponse() {
    }

    public LoginResponse(User user, String message) {
        this.userId = user.getUserId();
        this.name = user.getName();
        this.locationLat = user.getLocationLat();
        this.locationLng = user.getLocationLng();
        this.hasBall = user.getHasBall();
        this.createdAt = user.getCreatedAt();
        this.message = message;
    }

    // Getters and Setters
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

