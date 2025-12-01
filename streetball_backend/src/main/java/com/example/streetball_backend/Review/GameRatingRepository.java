package com.example.streetball_backend.Review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameRatingRepository extends JpaRepository<GameRating, Integer> {

    // 특정 게임에서 특정 리뷰어가 특정 리뷰이에게 준 평점 찾기
    Optional<GameRating> findByGame_GameIdAndReviewer_UserIdAndReviewee_UserId(
            Integer gameId, Integer reviewerId, Integer revieweeId);

    // 특정 게임에서 특정 리뷰어가 준 모든 평점 찾기
    List<GameRating> findByGame_GameIdAndReviewer_UserId(Integer gameId, Integer reviewerId);

    // 특정 게임의 모든 평점 찾기
    List<GameRating> findByGame_GameId(Integer gameId);

    // 특정 사용자가 받은 모든 평점 찾기
    List<GameRating> findByReviewee_UserId(Integer revieweeId);

    // 특정 사용자가 특정 역할로 받은 평점 찾기
    List<GameRating> findByReviewee_UserIdAndRevieweeRole(Integer revieweeId, RatingRole role);

    // 특정 사용자가 받은 평점 통계
    @Query("SELECT AVG(gr.rating) FROM GameRating gr WHERE gr.reviewee.userId = :userId AND gr.revieweeRole = :role")
    Double getAverageRatingByUserIdAndRole(@Param("userId") Integer userId, @Param("role") RatingRole role);

    @Query("SELECT COUNT(gr) FROM GameRating gr WHERE gr.reviewee.userId = :userId AND gr.revieweeRole = :role")
    Long getCountByUserIdAndRole(@Param("userId") Integer userId, @Param("role") RatingRole role);
}

