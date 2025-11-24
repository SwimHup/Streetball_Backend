package com.example.streetball_backend.Game;

public class NearbyGameRequest {
    private Double latitude;
    private Double longitude;
    private Integer radiusKm;

    // Constructors
    public NearbyGameRequest() {
    }

    public NearbyGameRequest(Double latitude, Double longitude, Integer radiusKm) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.radiusKm = radiusKm;
    }

    // Getters and Setters
    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getRadiusKm() {
        return radiusKm;
    }

    public void setRadiusKm(Integer radiusKm) {
        this.radiusKm = radiusKm;
    }
}

