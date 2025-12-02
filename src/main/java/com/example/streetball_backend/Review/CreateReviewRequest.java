package com.example.streetball_backend.Review;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "평점 생성 요청")
public class CreateReviewRequest {

    @NotNull(message = "게임 ID는 필수입니다")
    @Schema(description = "게임 ID", example = "1")
    private Integer gameId;

    @NotNull(message = "평가받는 사용자 이름은 필수입니다")
    @Schema(description = "평가받는 사용자 이름", example = "김철수")
    private String revieweeName;

    @NotNull(message = "역할은 필수입니다")
    @Schema(description = "평가받는 사용자의 역할 (PLAYER 또는 REFEREE)", example = "PLAYER")
    private RatingRole revieweeRole;

    @NotNull(message = "평점은 필수입니다")
    @Min(value = 1, message = "평점은 1 이상이어야 합니다")
    @Max(value = 5, message = "평점은 5 이하여야 합니다")
    @Schema(description = "평점 (1-5)", example = "5")
    private Integer rating;

    @Size(max = 500, message = "코멘트는 500자 이하여야 합니다")
    @Schema(description = "코멘트 (선택사항)", example = "좋은 플레이였습니다!")
    private String comment;

    // Constructors
    public CreateReviewRequest() {
    }

    public CreateReviewRequest(Integer gameId, String revieweeName, RatingRole revieweeRole, Integer rating, String comment) {
        this.gameId = gameId;
        this.revieweeName = revieweeName;
        this.revieweeRole = revieweeRole;
        this.rating = rating;
        this.comment = comment;
    }

    // Getters and Setters
    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public String getRevieweeName() {
        return revieweeName;
    }

    public void setRevieweeName(String revieweeName) {
        this.revieweeName = revieweeName;
    }

    public RatingRole getRevieweeRole() {
        return revieweeRole;
    }

    public void setRevieweeRole(RatingRole revieweeRole) {
        this.revieweeRole = revieweeRole;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

