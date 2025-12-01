package com.example.streetball_backend.Game;

import com.example.streetball_backend.Participation.ParticipationRole;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class GameResponse {
    private Integer gameId;
    private Integer courtId;
    private String courtName;
    private Integer maxPlayers;
    private Integer currentPlayers;
    private GameStatus status;
    private LocalDateTime scheduledTime;
    private LocalDateTime createdAt;
    private Integer refereeId;
    private Integer hostId;  // 방장 (playerIds의 첫 번째 참여자)
    private List<Integer> playerIds;
    private Double locationLat;
    private Double locationLng;

    // Constructors
    public GameResponse() {
    }

    public GameResponse(Game game) {
        this.gameId = game.getGameId();
        this.courtId = game.getCourt().getCourtId();
        this.courtName = game.getCourt().getCourtName();
        this.maxPlayers = game.getMaxPlayers();
        this.currentPlayers = game.getCurrentPlayers();
        this.status = game.getStatus();
        this.scheduledTime = game.getScheduledTime();
        this.createdAt = game.getCreatedAt();
        
        // 심판 정보
        if (game.getReferee() != null) {
            this.refereeId = game.getReferee().getUserId();
        }
        
        // 참여자 목록 (referee 제외, player와 spectator만 포함, 참여 시간순 정렬)
        this.playerIds = game.getParticipations().stream()
                .filter(p -> p.getRole() != ParticipationRole.referee)
                .sorted((p1, p2) -> p1.getJoinedAt().compareTo(p2.getJoinedAt()))
                .map(p -> p.getUser().getUserId())
                .collect(Collectors.toList());
        
        // 방장 (playerIds의 첫 번째 참여자)
        if (!this.playerIds.isEmpty()) {
            this.hostId = this.playerIds.get(0);
        }
        
        // 위도/경도
        this.locationLat = game.getLocationLat();
        this.locationLng = game.getLocationLng();
    }

    // Getters and Setters
    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public Integer getCourtId() {
        return courtId;
    }

    public void setCourtId(Integer courtId) {
        this.courtId = courtId;
    }

    public String getCourtName() {
        return courtName;
    }

    public void setCourtName(String courtName) {
        this.courtName = courtName;
    }

    public Integer getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(Integer maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public Integer getCurrentPlayers() {
        return currentPlayers;
    }

    public void setCurrentPlayers(Integer currentPlayers) {
        this.currentPlayers = currentPlayers;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getRefereeId() {
        return refereeId;
    }

    public void setRefereeId(Integer refereeId) {
        this.refereeId = refereeId;
    }

    public Integer getHostId() {
        return hostId;
    }

    public void setHostId(Integer hostId) {
        this.hostId = hostId;
    }

    public List<Integer> getPlayerIds() {
        return playerIds;
    }

    public void setPlayerIds(List<Integer> playerIds) {
        this.playerIds = playerIds;
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

