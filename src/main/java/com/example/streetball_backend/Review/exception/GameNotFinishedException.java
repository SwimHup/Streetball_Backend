package com.example.streetball_backend.Review.exception;

public class GameNotFinishedException extends RuntimeException {
    public GameNotFinishedException(String message) {
        super(message);
    }
}

