package com.travel.domain.product.controller;

import com.travel.domain.accommodation.dto.response.AccommodationDetailListResponse;
import com.travel.domain.product.dto.response.ProductDetailResponse;
import com.travel.domain.product.dto.response.ProductSimpleResponse;
import com.travel.domain.product.service.ProductService;
import com.travel.global.util.DateValidationUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Product API", description = "객실 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/accommodations/{accommodationId}")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "객실 리스트 조회", description = "숙박 아이디 별 객실을 조회합니다")
    @ApiResponse(description = "객실 리스트 조회 성공",
        content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = AccommodationDetailListResponse.class)))
    @GetMapping
    public ResponseEntity<AccommodationDetailListResponse> getAccommodationDetail(
        @PathVariable Long accommodationId,
        @RequestParam(required = false) LocalDate checkInDate,
        @RequestParam(required = false) LocalDate checkOutDate,
        @RequestParam(defaultValue = "2") int personNumber
    ) {
        checkInDate = DateValidationUtil.checkInDate(checkInDate);
        checkOutDate = DateValidationUtil.checkOutDate(checkInDate, checkOutDate);

        var response = productService
            .getAccommodationDetail(accommodationId, checkInDate, checkOutDate, personNumber);

        return ResponseEntity.ok(response);
    }


    @Operation(summary = "객실 디테일 조회", description = "객실 디테일을 조회합니다")
    @ApiResponse(description = "객실 디테일 조회 성공",
        content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ProductDetailResponse.class)))
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDetailResponse> getProductDetail(
        @PathVariable Long accommodationId,
        @PathVariable Long productId,
        @RequestParam(required = false) LocalDate checkInDate,
        @RequestParam(required = false) LocalDate checkOutDate,
        @RequestParam(defaultValue = "2") Integer personNumber
    ) {
        checkInDate = DateValidationUtil.checkInDate(checkInDate);
        checkOutDate = DateValidationUtil.checkOutDate(checkInDate, checkOutDate);

        var response = productService.getProductDetail(accommodationId, productId,
            checkInDate, checkOutDate, personNumber);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "객실 이름 검색 조회", description = "검색 이름에 해당하는 객실 리스트를 조회합니다")
    @ApiResponse(description = "객실 리스트 성공",
        content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ProductSimpleResponse.class)))
    @GetMapping("/search")
    public ResponseEntity<List<ProductSimpleResponse>> getSearchProduct(
        @PathVariable Long accommodationId,
        @RequestParam String keyword
    ) {
        var response = productService.getSearchProduct(accommodationId, keyword);
        return ResponseEntity.ok(response);
    }

}