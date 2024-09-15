package com.travel.domain.product.controller;

import com.travel.domain.accommodation.dto.response.AccommodationDetailListResponse;
import com.travel.domain.product.service.ProductService;
import com.travel.global.annotation.TokenMemberId;
import com.travel.global.util.DateValidationUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth Product API", description = "객실 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth/accommodations/{accommodationId}")
public class AuthProductController {

    private final ProductService productService;

    @Operation(summary = "객실 리스트 조회", description = "(회원 사용 가능)숙박 아이디 별 객실을 조회합니다")
    @ApiResponse(description = "객실 리스트 조회 성공",
        content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = AccommodationDetailListResponse.class)))
    @GetMapping
    public ResponseEntity<AccommodationDetailListResponse> getAccommodationDetailByAuth(
        @TokenMemberId Long tokenUserId,
        @PathVariable Long accommodationId,
        @RequestParam(required = false) LocalDate checkInDate,
        @RequestParam(required = false) LocalDate checkOutDate,
        @RequestParam(defaultValue = "2") int personNumber
    ) {
        checkInDate = DateValidationUtil.checkInDate(checkInDate);
        checkOutDate = DateValidationUtil.checkOutDate(checkInDate, checkOutDate);

        var response = productService
            .getAccommodationDetailByAuth(accommodationId, checkInDate, checkOutDate, personNumber,
                tokenUserId);

        return ResponseEntity.ok(response);
    }

}
