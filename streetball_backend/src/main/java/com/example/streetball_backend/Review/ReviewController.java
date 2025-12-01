package com.example.streetball_backend.Review;

import com.example.streetball_backend.Game.ErrorResponse;
import com.example.streetball_backend.Review.exception.*;
import com.example.streetball_backend.config.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@Tag(name = "Review", description = "평점 관리 API")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 평점 생성
     */
    @PostMapping
    @Operation(summary = "평점 생성", description = "종료된 게임의 참여자나 심판에게 평점을 남깁니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "평점 생성 성공",
                    content = @Content(schema = @Schema(implementation = GameRatingResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "중복된 평점",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> createReview(
            @Parameter(description = "JWT 토큰", required = true)
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody CreateReviewRequest request) {
        try {
            Integer userId = jwtUtil.extractUserId(token.replace("Bearer ", ""));
            GameRatingResponse response = reviewService.createReview(userId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("INVALID_REQUEST", e.getMessage()));
        } catch (GameNotFinishedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse("GAME_NOT_FINISHED", e.getMessage()));
        } catch (UnauthorizedReviewException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse("UNAUTHORIZED", e.getMessage()));
        } catch (DuplicateReviewException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("DUPLICATE_REVIEW", e.getMessage()));
        }
    }

    /**
     * 평점 조회 (단일)
     */
    @GetMapping("/{ratingId}")
    @Operation(summary = "평점 조회", description = "특정 평점의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = GameRatingResponse.class))),
            @ApiResponse(responseCode = "404", description = "평점을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> getReview(
            @Parameter(description = "평점 ID", required = true)
            @PathVariable Integer ratingId) {
        try {
            GameRatingResponse response = reviewService.getReview(ratingId);
            return ResponseEntity.ok(response);
        } catch (ReviewNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("REVIEW_NOT_FOUND", e.getMessage()));
        }
    }

    /**
     * 내가 특정 게임에서 남긴 평점 목록 조회
     */
    @GetMapping("/my-reviews/game/{gameId}")
    @Operation(summary = "내가 남긴 평점 조회", description = "특정 게임에서 내가 남긴 모든 평점을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> getMyReviewsForGame(
            @Parameter(description = "JWT 토큰", required = true)
            @RequestHeader("Authorization") String token,
            @Parameter(description = "게임 ID", required = true)
            @PathVariable Integer gameId) {
        try {
            Integer userId = jwtUtil.extractUserId(token.replace("Bearer ", ""));
            List<GameRatingResponse> responses = reviewService.getMyReviewsForGame(userId, gameId);
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("ERROR", e.getMessage()));
        }
    }

    /**
     * 특정 게임의 모든 평점 조회
     */
    @GetMapping("/game/{gameId}")
    @Operation(summary = "게임 평점 목록 조회", description = "특정 게임의 모든 평점을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    public ResponseEntity<List<GameRatingResponse>> getGameReviews(
            @Parameter(description = "게임 ID", required = true)
            @PathVariable Integer gameId) {
        List<GameRatingResponse> responses = reviewService.getGameReviews(gameId);
        return ResponseEntity.ok(responses);
    }

    /**
     * 특정 사용자의 평점 요약 조회
     */
    @GetMapping("/user/{userId}/summary")
    @Operation(summary = "사용자 평점 요약 조회", description = "특정 사용자의 평점 통계를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = UserReviewSummaryResponse.class))),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> getUserReviewSummary(
            @Parameter(description = "사용자 ID", required = true)
            @PathVariable Integer userId) {
        try {
            UserReviewSummaryResponse response = reviewService.getUserReviewSummary(userId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("USER_NOT_FOUND", e.getMessage()));
        }
    }

    /**
     * 평점 수정
     */
    @PutMapping("/{ratingId}")
    @Operation(summary = "평점 수정", description = "자신이 작성한 평점을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공",
                    content = @Content(schema = @Schema(implementation = GameRatingResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "평점을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> updateReview(
            @Parameter(description = "JWT 토큰", required = true)
            @RequestHeader("Authorization") String token,
            @Parameter(description = "평점 ID", required = true)
            @PathVariable Integer ratingId,
            @Valid @RequestBody UpdateReviewRequest request) {
        try {
            Integer userId = jwtUtil.extractUserId(token.replace("Bearer ", ""));
            GameRatingResponse response = reviewService.updateReview(userId, ratingId, request);
            return ResponseEntity.ok(response);
        } catch (ReviewNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("REVIEW_NOT_FOUND", e.getMessage()));
        } catch (UnauthorizedReviewException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse("UNAUTHORIZED", e.getMessage()));
        }
    }

    /**
     * 평점 삭제
     */
    @DeleteMapping("/{ratingId}")
    @Operation(summary = "평점 삭제", description = "자신이 작성한 평점을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "평점을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> deleteReview(
            @Parameter(description = "JWT 토큰", required = true)
            @RequestHeader("Authorization") String token,
            @Parameter(description = "평점 ID", required = true)
            @PathVariable Integer ratingId) {
        try {
            Integer userId = jwtUtil.extractUserId(token.replace("Bearer ", ""));
            reviewService.deleteReview(userId, ratingId);
            return ResponseEntity.noContent().build();
        } catch (ReviewNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("REVIEW_NOT_FOUND", e.getMessage()));
        } catch (UnauthorizedReviewException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse("UNAUTHORIZED", e.getMessage()));
        }
    }
}

