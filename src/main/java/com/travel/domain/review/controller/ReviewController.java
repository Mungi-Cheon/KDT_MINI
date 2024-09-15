package com.travel.domain.review.controller;

import com.travel.domain.review.dto.response.AccommodationReviewResponseList;
import com.travel.domain.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Review API", description = "리뷰 API")
@RequestMapping("/api/review/{accommodationId}")
@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "리뷰 조회", description = "숙박 아이디 별 리뷰를 조회합니다")
    @ApiResponse(description = "리뷰 리스트 조회 성공",
        content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = AccommodationReviewResponseList.class)))
    @GetMapping
    public ResponseEntity<AccommodationReviewResponseList> getReviewList(
        @PathVariable Long accommodationId) {

        return  ResponseEntity.ok(reviewService.getReviewList(accommodationId));
    }
}
