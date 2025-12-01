package com.example.streetball_backend.Review;

import com.example.streetball_backend.Game.Game;
import com.example.streetball_backend.User.User;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "GameRating",
        uniqueConstraints = @UniqueConstraint(name = "uk_game_reviewer_reviewee",
                columnNames = {"game_id", "reviewer_id", "reviewee_id"}))
public class GameRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rating_id")
    private Integer ratingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id", nullable = false)
    private User reviewer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewee_id", nullable = false)
    private User reviewee;

    @Enumerated(EnumType.STRING)
    @Column(name = "reviewee_role", nullable = false, length = 20)
    private RatingRole revieweeRole; // 평가받는 사람의 역할 (심판인지 참여자인지)

    @Column(name = "rating", nullable = false)
    private Integer rating; // 1-5점

    @Column(name = "comment", length = 500)
    private String comment;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public GameRating() {
    }

    public GameRating(Game game, User reviewer, User reviewee, RatingRole revieweeRole, Integer rating, String comment) {
        this.game = game;
        this.reviewer = reviewer;
        this.reviewee = reviewee;
        this.revieweeRole = revieweeRole;
        this.rating = rating;
        this.comment = comment;
    }

    // Getters and Setters
    public Integer getRatingId() {
        return ratingId;
    }

    public void setRatingId(Integer ratingId) {
        this.ratingId = ratingId;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public User getReviewer() {
        return reviewer;
    }

    public void setReviewer(User reviewer) {
        this.reviewer = reviewer;
    }

    public User getReviewee() {
        return reviewee;
    }

    public void setReviewee(User reviewee) {
        this.reviewee = reviewee;
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

