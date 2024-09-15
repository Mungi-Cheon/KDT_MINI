package com.travel.domain.like.controller;

import com.travel.domain.accommodation.dto.response.AccommodationResponse;
import com.travel.domain.like.dto.request.LikeRequest;
import com.travel.domain.like.dto.response.LikeResponse;
import com.travel.domain.like.service.LikeService;
import com.travel.global.annotation.TokenMemberId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/like")
@Tag(name = "Like API", description = "좋아요 API")
public class LikeController {

    private final LikeService likeService;

    @Operation(summary = "좋아요 클릭", description = "좋아요 버튼을 클릭합니다.")
    @ApiResponse(content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = LikeResponse.class)))
    @PostMapping
    public LikeResponse clickLike(@TokenMemberId Long userId, @RequestBody LikeRequest likeRequest) {
        return likeService.clickLike(userId, likeRequest);
    }

    @Operation(summary = "좋아요 목록 조회", description = "좋아요를 클릭한 목록을 조회합니다.")
    @ApiResponse(content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = AccommodationResponse.class)))
    @GetMapping
    public ResponseEntity<List<AccommodationResponse>> getMyLikedAccommodations(
        @TokenMemberId Long userId) {
        List<AccommodationResponse> likedAccommodations = likeService.getLikedAccommodations(userId);
        return ResponseEntity.ok(likedAccommodations);
    }
}
