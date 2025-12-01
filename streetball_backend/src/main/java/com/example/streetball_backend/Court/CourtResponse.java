package com.example.streetball_backend.Court;


public class CourtResponse {
    private Integer courtId;
    private String courtName;
    private Double locationLat;
    private Double locationLng;
    private Boolean isIndoor;

    public CourtResponse(Court court) {
        this.courtId = court.getCourtId();
        this.courtName = court.getCourtName();
        this.locationLat = court.getLocationLat();
        this.locationLng = court.getLocationLng();
        this.isIndoor = court.getIsIndoor();
    }

    public Integer getCourtId() {
        return courtId;
    }
    public String getCourtName() {
        return courtName;
    }
    public Double getLocationLat() {
        return locationLat;
    }
    public Double getLocationLng() {
        return locationLng;
    }
    public Boolean getIsIndoor() {
        return isIndoor;
    }
}
