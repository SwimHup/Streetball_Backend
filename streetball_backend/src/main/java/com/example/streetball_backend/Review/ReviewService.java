package com.example.streetball_backend.Review;

import com.example.streetball_backend.Game.Game;
import com.example.streetball_backend.Game.GameRepository;
import com.example.streetball_backend.Game.GameStatus;
import com.example.streetball_backend.Participation.ParticipationRepository;
import com.example.streetball_backend.Review.exception.*;
import com.example.streetball_backend.User.User;
import com.example.streetball_backend.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private GameRatingRepository gameRatingRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ParticipationRepository participationRepository;

    /**
     * 평점 생성
     */
    @Transactional
    public GameRatingResponse createReview(Integer reviewerId, CreateReviewRequest request) {
        // 1. 게임 존재 확인
        Game game = gameRepository.findById(request.getGameId())
                .orElseThrow(() -> new IllegalArgumentException("게임을 찾을 수 없습니다."));

        // 2. 게임이 종료된 상태인지 확인
        if (game.getStatus() != GameStatus.게임_종료) {
            throw new GameNotFinishedException("종료된 게임에만 평점을 남길 수 있습니다.");
        }

        // 3. 리뷰어가 해당 게임에 참여했는지 확인
        boolean reviewerParticipated = participationRepository.existsByGameGameIdAndUserUserId(
                game.getGameId(), reviewerId);
        if (!reviewerParticipated) {
            throw new UnauthorizedReviewException("해당 게임에 참여한 사용자만 평점을 남길 수 있습니다.");
        }

        // 4. 피평가자 확인 (이름으로 조회)
        User reviewee = userRepository.findByName(request.getRevieweeName())
                .orElseThrow(() -> new IllegalArgumentException("평가받을 사용자를 찾을 수 없습니다."));

        // 5. 리뷰어 정보 가져오기
        User reviewer = userRepository.findById(reviewerId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰어를 찾을 수 없습니다."));

        // 6. 자기 자신에게 평점을 주려는 경우 차단
        if (reviewer.getName().equals(reviewee.getName())) {
            throw new UnauthorizedReviewException("자기 자신에게는 평점을 줄 수 없습니다.");
        }

        // 7. 피평가자가 해당 게임에 해당 역할로 참여했는지 확인
        if (request.getRevieweeRole() == RatingRole.REFEREE) {
            // 심판인 경우 - game.referee 확인
            if (game.getReferee() == null || !game.getReferee().getUserId().equals(reviewee.getUserId())) {
                throw new UnauthorizedReviewException("해당 사용자는 이 게임의 심판이 아닙니다.");
            }
        } else {
            // 참여자인 경우 - participation 확인
            boolean revieweeParticipated = participationRepository.existsByGameGameIdAndUserUserId(
                    game.getGameId(), reviewee.getUserId());
            if (!revieweeParticipated) {
                throw new UnauthorizedReviewException("해당 사용자는 이 게임의 참여자가 아닙니다.");
            }
        }

        // 8. 이미 평점을 남긴 경우 차단
        if (gameRatingRepository.findByGame_GameIdAndReviewer_UserIdAndReviewee_UserId(
                game.getGameId(), reviewerId, reviewee.getUserId()).isPresent()) {
            throw new DuplicateReviewException("이미 해당 사용자에게 평점을 남겼습니다.");
        }

        // 9. GameRating 생성
        GameRating gameRating = new GameRating(
                game, reviewer, reviewee,
                request.getRevieweeRole(),
                request.getRating(),
                request.getComment()
        );
        gameRating = gameRatingRepository.save(gameRating);

        // 10. Review 통계 업데이트
        updateReviewStatistics(reviewee.getUserId(), request.getRevieweeRole());

        return new GameRatingResponse(gameRating);
    }

    /**
     * 평점 조회 (단일)
     */
    @Transactional(readOnly = true)
    public GameRatingResponse getReview(Integer ratingId) {
        GameRating gameRating = gameRatingRepository.findById(ratingId)
                .orElseThrow(() -> new ReviewNotFoundException("평점을 찾을 수 없습니다."));
        return new GameRatingResponse(gameRating);
    }

    /**
     * 내가 특정 게임에서 남긴 평점 목록 조회
     */
    @Transactional(readOnly = true)
    public List<GameRatingResponse> getMyReviewsForGame(Integer reviewerId, Integer gameId) {
        List<GameRating> ratings = gameRatingRepository.findByGame_GameIdAndReviewer_UserId(gameId, reviewerId);
        return ratings.stream()
                .map(GameRatingResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 특정 게임의 모든 평점 조회
     */
    @Transactional(readOnly = true)
    public List<GameRatingResponse> getGameReviews(Integer gameId) {
        List<GameRating> ratings = gameRatingRepository.findByGame_GameId(gameId);
        return ratings.stream()
                .map(GameRatingResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 특정 사용자가 받은 평점 요약 조회
     */
    @Transactional
    public UserReviewSummaryResponse getUserReviewSummary(Integer userId) {
        // User 존재 확인 및 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // Review 조회 또는 생성
        Review review = reviewRepository.findById(userId).orElse(null);
        if (review == null) {
            review = new Review();
            review.setUserId(userId);
            review.setPlayScore(BigDecimal.ZERO);
            review.setPlayCount(0);
            review.setRefScore(BigDecimal.ZERO);
            review.setRefCount(0);
            review = reviewRepository.save(review);
            review.markNotNew();
        } else {
            review.markNotNew();
        }

        return new UserReviewSummaryResponse(review, user.getName());
    }

    /**
     * 평점 수정
     */
    @Transactional
    public GameRatingResponse updateReview(Integer reviewerId, Integer ratingId, UpdateReviewRequest request) {
        // 1. 평점 찾기
        GameRating gameRating = gameRatingRepository.findById(ratingId)
                .orElseThrow(() -> new ReviewNotFoundException("평점을 찾을 수 없습니다."));

        // 2. 권한 확인 (본인이 작성한 평점인지)
        if (!gameRating.getReviewer().getUserId().equals(reviewerId)) {
            throw new UnauthorizedReviewException("본인이 작성한 평점만 수정할 수 있습니다.");
        }

        // 3. 이전 평점 저장 (통계 업데이트용)
        Integer oldRating = gameRating.getRating();

        // 4. 평점 수정
        gameRating.setRating(request.getRating());
        gameRating.setComment(request.getComment());
        gameRating.setUpdatedAt(LocalDateTime.now());
        gameRating = gameRatingRepository.save(gameRating);

        // 5. Review 통계 업데이트 (평점이 변경된 경우)
        if (!oldRating.equals(request.getRating())) {
            updateReviewStatistics(gameRating.getReviewee().getUserId(), gameRating.getRevieweeRole());
        }

        return new GameRatingResponse(gameRating);
    }

    /**
     * 평점 삭제
     */
    @Transactional
    public void deleteReview(Integer reviewerId, Integer ratingId) {
        // 1. 평점 찾기
        GameRating gameRating = gameRatingRepository.findById(ratingId)
                .orElseThrow(() -> new ReviewNotFoundException("평점을 찾을 수 없습니다."));

        // 2. 권한 확인 (본인이 작성한 평점인지)
        if (!gameRating.getReviewer().getUserId().equals(reviewerId)) {
            throw new UnauthorizedReviewException("본인이 작성한 평점만 삭제할 수 있습니다.");
        }

        // 3. 피평가자와 역할 저장 (통계 업데이트용)
        Integer revieweeId = gameRating.getReviewee().getUserId();
        RatingRole role = gameRating.getRevieweeRole();

        // 4. 평점 삭제
        gameRatingRepository.delete(gameRating);

        // 5. Review 통계 업데이트
        updateReviewStatistics(revieweeId, role);
    }

    /**
     * Review 통계 업데이트 (평균 평점 및 개수)
     */
    private void updateReviewStatistics(Integer userId, RatingRole role) {
        // User 존재 확인
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // Review 조회 또는 생성
        Review review = reviewRepository.findById(userId).orElse(null);
        boolean isNewReview = false;
        if (review == null) {
            review = new Review();
            review.setUserId(userId);
            review.setPlayScore(BigDecimal.ZERO);
            review.setPlayCount(0);
            review.setRefScore(BigDecimal.ZERO);
            review.setRefCount(0);
            isNewReview = true;
        } else {
            review.markNotNew();
        }

        // 해당 역할의 평점 통계 재계산
        Double avgRating = gameRatingRepository.getAverageRatingByUserIdAndRole(userId, role);
        Long count = gameRatingRepository.getCountByUserIdAndRole(userId, role);

        if (role == RatingRole.PLAYER) {
            review.setPlayScore(avgRating != null ? 
                    BigDecimal.valueOf(avgRating).setScale(2, RoundingMode.HALF_UP) : 
                    BigDecimal.ZERO);
            review.setPlayCount(count != null ? count.intValue() : 0);
        } else { // REFEREE
            review.setRefScore(avgRating != null ? 
                    BigDecimal.valueOf(avgRating).setScale(2, RoundingMode.HALF_UP) : 
                    BigDecimal.ZERO);
            review.setRefCount(count != null ? count.intValue() : 0);
        }

        review = reviewRepository.save(review);
        if (isNewReview) {
            review.markNotNew();
        }
    }
}

