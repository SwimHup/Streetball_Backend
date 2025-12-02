package com.example.streetball_backend.User;

import java.time.LocalDateTime;

public class UserResponse {
    private Integer userId;
    private String name;
    private Double locationLat;
    private Double locationLng;
    private Boolean hasBall;
    private LocalDateTime createdAt;

    // Constructors
    public UserResponse() {
    }

    public UserResponse(User user) {
        this.userId = user.getUserId();
        this.name = user.getName();
        this.locationLat = user.getLocationLat();
        this.locationLng = user.getLocationLng();
        this.hasBall = user.getHasBall();
        this.createdAt = user.getCreatedAt();
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
}

