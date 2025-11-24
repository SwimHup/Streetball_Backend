package com.example.streetball_backend.Game;

import java.time.LocalDateTime;

public class GameCreationRequest {
    private Integer courtId;
    private Integer creatorUserId;  // 모임 생성자의 user_id
    private Integer maxPlayers;
    private LocalDateTime scheduledTime;

    // Constructors
    public GameCreationRequest() {
    }

    public GameCreationRequest(Integer courtId, Integer creatorUserId, Integer maxPlayers, LocalDateTime scheduledTime) {
        this.courtId = courtId;
        this.creatorUserId = creatorUserId;
        this.maxPlayers = maxPlayers;
        this.scheduledTime = scheduledTime;
    }

    // Getters and Setters
    public Integer getCourtId() {
        return courtId;
    }

    public void setCourtId(Integer courtId) {
        this.courtId = courtId;
    }

    public Integer getCreatorUserId() {
        return creatorUserId;
    }

    public void setCreatorUserId(Integer creatorUserId) {
        this.creatorUserId = creatorUserId;
    }

    public Integer getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(Integer maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }
}

