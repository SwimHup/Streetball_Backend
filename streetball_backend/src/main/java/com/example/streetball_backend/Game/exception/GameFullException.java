package com.example.streetball_backend.Game.exception;

public class GameFullException extends RuntimeException {
    public GameFullException(String message) {
        super(message);
    }
}

