package com.example.streetball_backend.Game;

import com.example.streetball_backend.Participation.ParticipationRole;

public class JoinGameRequest {
    private Integer userId;
    private ParticipationRole role;

    // Constructors
    public JoinGameRequest() {
    }

    public JoinGameRequest(Integer userId, ParticipationRole role) {
        this.userId = userId;
        this.role = role;
    }

    // Getters and Setters
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public ParticipationRole getRole() {
        return role;
    }

    public void setRole(ParticipationRole role) {
        this.role = role;
    }
}

