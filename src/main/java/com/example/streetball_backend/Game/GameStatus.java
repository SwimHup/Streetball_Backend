package com.example.streetball_backend.Game;

public enum GameStatus {
    모집_중("모집 중"),
    모집_완료("모집 완료"),
    게임_종료("게임 종료");

    private final String displayName;

    GameStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

