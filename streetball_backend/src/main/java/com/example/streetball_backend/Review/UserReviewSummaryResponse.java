package com.example.streetball_backend.Review;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "사용자 평점 요약")
public class UserReviewSummaryResponse {

    @Schema(description = "사용자 ID", example = "1")
    private Integer userId;

    @Schema(description = "사용자 이름", example = "홍길동")
    private String userName;

    @Schema(description = "참여자로서의 평균 평점", example = "4.5")
    private BigDecimal playScore;

    @Schema(description = "참여자로서의 평점 개수", example = "10")
    private Integer playCount;

    @Schema(description = "심판으로서의 평균 평점", example = "4.8")
    private BigDecimal refScore;

    @Schema(description = "심판으로서의 평점 개수", example = "5")
    private Integer refCount;

    // Constructors
    public UserReviewSummaryResponse() {
    }

    public UserReviewSummaryResponse(Review review) {
        this.userId = review.getUserId();
        this.userName = review.getUser().getName();
        this.playScore = review.getPlayScore();
        this.playCount = review.getPlayCount();
        this.refScore = review.getRefScore();
        this.refCount = review.getRefCount();
    }

    // Getters and Setters
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public BigDecimal getPlayScore() {
        return playScore;
    }

    public void setPlayScore(BigDecimal playScore) {
        this.playScore = playScore;
    }

    public Integer getPlayCount() {
        return playCount;
    }

    public void setPlayCount(Integer playCount) {
        this.playCount = playCount;
    }

    public BigDecimal getRefScore() {
        return refScore;
    }

    public void setRefScore(BigDecimal refScore) {
        this.refScore = refScore;
    }

    public Integer getRefCount() {
        return refCount;
    }

    public void setRefCount(Integer refCount) {
        this.refCount = refCount;
    }
}

