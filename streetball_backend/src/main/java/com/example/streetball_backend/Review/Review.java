package com.example.streetball_backend.Review;

import com.example.streetball_backend.User.User;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "Review")
public class Review {

    @Id
    @Column(name = "user_id")
    private Integer userId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "play_score", precision = 3, scale = 2)
    private BigDecimal playScore = BigDecimal.ZERO;

    @Column(name = "play_count", nullable = false)
    private Integer playCount = 0;

    @Column(name = "ref_score", precision = 3, scale = 2)
    private BigDecimal refScore = BigDecimal.ZERO;

    @Column(name = "ref_count", nullable = false)
    private Integer refCount = 0;

    // Constructors
    public Review() {
    }

    public Review(User user) {
        this.user = user;
        this.userId = user.getUserId();
        this.playScore = BigDecimal.ZERO;
        this.playCount = 0;
        this.refScore = BigDecimal.ZERO;
        this.refCount = 0;
    }

    // Getters and Setters
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
