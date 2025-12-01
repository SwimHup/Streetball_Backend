package com.example.streetball_backend.Game;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "에러 응답")
public class ErrorResponse {
    
    @Schema(description = "에러 코드", example = "ALREADY_PARTICIPATING")
    private String error;
    
    @Schema(description = "에러 메시지", example = "이미 참여한 게임입니다.")
    private String message;

    public ErrorResponse(String error, String message) {
        this.error = error;
        this.message = message;
    }

    // Getters and Setters
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

