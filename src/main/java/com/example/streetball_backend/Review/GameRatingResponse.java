package com.example.streetball_backend.Review;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "평점 응답")
public class GameRatingResponse {

    @Schema(description = "평점 ID", example = "1")
    private Integer ratingId;

    @Schema(description = "게임 ID", example = "1")
    private Integer gameId;

    @Schema(description = "평가자 이름", example = "홍길동")
    private String reviewerName;

    @Schema(description = "피평가자 이름", example = "김철수")
    private String revieweeName;

    @Schema(description = "피평가자 역할", example = "PLAYER")
    private RatingRole revieweeRole;

    @Schema(description = "평점 (1-5)", example = "5")
    private Integer rating;

    @Schema(description = "코멘트", example = "훌륭한 플레이였습니다!")
    private String comment;

    @Schema(description = "생성 시간", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "수정 시간", example = "2024-01-15T11:30:00")
    private LocalDateTime updatedAt;

    // Constructors
    public GameRatingResponse() {
    }

    public GameRatingResponse(GameRating gameRating) {
        this.ratingId = gameRating.getRatingId();
        this.gameId = gameRating.getGame().getGameId();
        this.reviewerName = gameRating.getReviewer().getName();
        this.revieweeName = gameRating.getReviewee().getName();
        this.revieweeRole = gameRating.getRevieweeRole();
        this.rating = gameRating.getRating();
        this.comment = gameRating.getComment();
        this.createdAt = gameRating.getCreatedAt();
        this.updatedAt = gameRating.getUpdatedAt();
    }

    // Getters and Setters
    public Integer getRatingId() {
        return ratingId;
    }

    public void setRatingId(Integer ratingId) {
        this.ratingId = ratingId;
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

