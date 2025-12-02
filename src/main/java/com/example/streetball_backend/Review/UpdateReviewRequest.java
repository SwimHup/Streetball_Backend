package com.example.streetball_backend.Review;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "평점 수정 요청")
public class UpdateReviewRequest {

    @NotNull(message = "평점은 필수입니다")
    @Min(value = 1, message = "평점은 1 이상이어야 합니다")
    @Max(value = 5, message = "평점은 5 이하여야 합니다")
    @Schema(description = "평점 (1-5)", example = "4")
    private Integer rating;

    @Size(max = 500, message = "코멘트는 500자 이하여야 합니다")
    @Schema(description = "코멘트 (선택사항)", example = "수정된 코멘트입니다.")
    private String comment;

    // Constructors
    public UpdateReviewRequest() {
    }

    public UpdateReviewRequest(Integer rating, String comment) {
        this.rating = rating;
        this.comment = comment;
    }

    // Getters and Setters
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

