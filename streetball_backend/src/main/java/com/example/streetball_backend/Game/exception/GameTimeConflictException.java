package com.example.streetball_backend.Game.exception;

public class GameTimeConflictException extends RuntimeException {
    public GameTimeConflictException(String message) {
        super(message);
    }
}

