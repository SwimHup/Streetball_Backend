package com.example.streetball_backend.Game.exception;

public class AlreadyParticipatingException extends RuntimeException {
    public AlreadyParticipatingException(String message) {
        super(message);
    }
}

