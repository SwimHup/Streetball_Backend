package com.example.streetball_backend.Game;

import com.example.streetball_backend.Participation.ParticipationRole;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Schema(description = "게임 응답")
public class GameResponse {
    
    @Schema(description = "게임 ID", example = "1")
    private Integer gameId;
    
    @Schema(description = "코트 ID", example = "10")
    private Integer courtId;
    
    @Schema(description = "코트 이름", example = "서울 농구장")
    private String courtName;
    
    @Schema(description = "최대 인원", example = "10")
    private Integer maxPlayers;
    
    @Schema(description = "현재 인원", example = "3")
    private Integer currentPlayers;
    
    @Schema(description = "게임 상태", example = "모집_중")
    private GameStatus status;
    
    @Schema(description = "예정 시간", example = "2025-12-05T14:00:00")
    private LocalDateTime scheduledTime;
    
    @Schema(description = "생성 시간", example = "2025-12-01T10:30:00")
    private LocalDateTime createdAt;
    
    @Schema(description = "심판 name (null 가능)", example = "test")
    private String referee;
    
    @Schema(description = "방장 name (첫 번째 참여자)", example = "test")
    private String hostName;
    
    @Schema(description = "플레이어 목록", example = "[test, test2, test3]")
    private List<String> playerNames;
    
    @Schema(description = "관전자 목록", example = "[spectator1, spectator2]")
    private List<String> spectatorNames;
    
    @Schema(description = "위도", example = "37.5665")
    private Double locationLat;
    
    @Schema(description = "경도", example = "126.9780")
    private Double locationLng;
    
    @Schema(description = "참여자 중 공을 가진 사람이 있는지 여부", example = "true")
    private Boolean hasBall;

    // Constructors
    public GameResponse() {
    }

    public GameResponse(Game game) {
        this.gameId = game.getGameId();
        this.courtId = game.getCourt().getCourtId();
        this.courtName = game.getCourt().getCourtName();
        this.maxPlayers = game.getMaxPlayers();
        
        // 현재 player 수를 participations에서 직접 계산 (player 역할만 카운트)
        this.currentPlayers = (int) game.getParticipations().stream()
                .filter(p -> p.getRole() == ParticipationRole.player)
                .count();
        
        this.status = game.getStatus();
        this.scheduledTime = game.getScheduledTime();
        this.createdAt = game.getCreatedAt();
        
        // 심판 정보
        if (game.getReferee() != null) {
            this.referee = game.getReferee().getName();
        }
        
        // 플레이어 목록 (player 역할만, 참여 시간순 정렬)
        this.playerNames = game.getParticipations().stream()
                .filter(p -> p.getRole() == ParticipationRole.player)
                .sorted((p1, p2) -> p1.getJoinedAt().compareTo(p2.getJoinedAt()))
                .map(p -> p.getUser().getName())
                .collect(Collectors.toList());
        
        // 관전자 목록 (spectator 역할만, 참여 시간순 정렬)
        this.spectatorNames = game.getParticipations().stream()
                .filter(p -> p.getRole() == ParticipationRole.spectator)
                .sorted((p1, p2) -> p1.getJoinedAt().compareTo(p2.getJoinedAt()))
                .map(p -> p.getUser().getName())
                .collect(Collectors.toList());
        
        // 방장 (playerNames의 첫 번째 참여자)
        if (!this.playerNames.isEmpty()) {
            this.hostName = this.playerNames.get(0);
        }
        
        // 위도/경도
        this.locationLat = game.getLocationLat();
        this.locationLng = game.getLocationLng();
        
        // 참여자 중 한 명이라도 공을 가지고 있는지 확인
        this.hasBall = game.getParticipations().stream()
                .anyMatch(p -> p.getUser().getHasBall() != null && p.getUser().getHasBall());
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

    public String getReferee() {
        return referee;
    }

    public void setReferee(String referee) {
        this.referee = referee;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public List<String> getPlayerNames() {
        return playerNames;
    }

    public void setPlayerNames(List<String> playerNames) {
        this.playerNames = playerNames;
    }

    public List<String> getSpectatorNames() {
        return spectatorNames;
    }

    public void setSpectatorNames(List<String> spectatorNames) {
        this.spectatorNames = spectatorNames;
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

