package com.example.streetball_backend.Review;

import jakarta.persistence.*;
import org.springframework.data.domain.Persistable;

import java.math.BigDecimal;

@Entity
@Table(name = "Review")
public class Review implements Persistable<Integer> {

    @Id
    @Column(name = "user_id")
    private Integer userId;
    
    @Transient
    private boolean isNew = true;

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

    // Persistable 인터페이스 구현
    @Override
    public Integer getId() {
        return userId;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }
    
    public void markNotNew() {
        this.isNew = false;
    }

    // Getters and Setters
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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
