package com.travel.domain.review.controller;

import com.travel.domain.review.dto.request.ReviewRequest;
import com.travel.domain.review.dto.response.AccommodationReviewResponseList;
import com.travel.domain.review.dto.response.DeleteReviewResponse;
import com.travel.domain.review.dto.response.ReviewResponse;
import com.travel.domain.review.dto.response.UpdateReviewResponse;
import com.travel.domain.review.service.ReviewService;
import com.travel.global.annotation.TokenMemberId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth Review API", description = "리뷰 API")
@RestController
@RequestMapping("api/auth/review")
@RequiredArgsConstructor
public class AuthReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "리뷰 작성", description = "리뷰를 작성합니다.")
    @ApiResponse(description = "리뷰 작성 성공",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ReviewResponse.class)))
    @PostMapping("/{accommodationId}")
    public ResponseEntity<ReviewResponse> createReview(
            @TokenMemberId Long tokenUserId,
            @PathVariable Long accommodationId,
            @RequestBody @Valid ReviewRequest reviewRequest) {

        return ResponseEntity.ok(reviewService.createReview(tokenUserId, accommodationId,
                reviewRequest));
    }

    @Operation(summary = "리뷰 수정", description = "리뷰를 수정합니다.")
    @ApiResponse(description = "리뷰 수정 성공",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UpdateReviewResponse.class)))
    @PutMapping("/{accommodationId}/{reviewId}")
    public ResponseEntity<UpdateReviewResponse> updateReview(
            @TokenMemberId Long tokenUserId,
            @PathVariable Long accommodationId,
            @PathVariable Long reviewId,
            @RequestBody @Valid ReviewRequest reviewRequest) {
        return ResponseEntity.ok(reviewService.updateReview(tokenUserId, accommodationId,
                reviewRequest, reviewId));
    }

    @Operation(summary = "리뷰 삭제", description = "리뷰를 삭제합니다.")
    @ApiResponse(description = "리뷰 삭제 성공",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = DeleteReviewResponse.class)))
    @DeleteMapping("/{accommodationId}/{reviewId}")
    public ResponseEntity<DeleteReviewResponse> deleteReview(
            @TokenMemberId Long tokenUserId,
            @PathVariable Long accommodationId,
            @PathVariable Long reviewId) {

        return ResponseEntity.ok(
                reviewService.deleteReview(tokenUserId, accommodationId, reviewId));
    }

    @Operation(summary = "내 리뷰 조회", description = "내 리뷰를 조회합니다.")
    @ApiResponse(description = "리뷰 조회 성공",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ReviewResponse.class)))
    @GetMapping
    public ResponseEntity<List<ReviewResponse>> getMyReviewList(
            @TokenMemberId Long tokenUserId) {
        return ResponseEntity.ok(reviewService.getMyReviewList(tokenUserId));
    }

    @Operation(summary = "특정 숙소 id의 내 리뷰 조회", description = "특정 숙소 id의 내 리뷰를 조회합니다.")
    @ApiResponse(description = "특정 숙소 id의 내 리뷰 조회 성공",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ReviewResponse.class)))
    @GetMapping("/{accommodationId}")
    public ResponseEntity<AccommodationReviewResponseList> getReview(
            @TokenMemberId Long tokenUserId,
            @PathVariable Long accommodationId) {
        return ResponseEntity.ok(reviewService.getReview(tokenUserId, accommodationId));
    }

}
