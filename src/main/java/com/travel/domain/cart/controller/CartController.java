package com.travel.domain.cart.controller;

import com.travel.domain.accommodation.dto.response.AccommodationResponse;
import com.travel.domain.cart.dto.request.CartRequest;
import com.travel.domain.cart.dto.response.CartResponse;
import com.travel.domain.cart.service.CartService;
import com.travel.global.annotation.TokenMemberId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Cart API", description = "장바구니 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/cart")
public class CartController {

    private final CartService cartService;

    @Operation(summary = "장바구니 추가", description = "장바구니에 품목을 추가합니다.")
    @ApiResponse(content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = CartResponse.class)))
    @PostMapping
    public ResponseEntity<CartResponse> addToCart(
        @TokenMemberId Long tokenMemberId, @RequestBody CartRequest cartRequest) {
        CartResponse cartResponse = cartService.addToCart(tokenMemberId, cartRequest);
        return ResponseEntity.ok(cartResponse);
    }

    @Operation(summary = "장바구니 조회", description = "장바구니를 조회합니다.")
    @ApiResponse(content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = CartResponse.class)))
    @GetMapping
    public ResponseEntity<List<CartResponse>> getCartByMemberId(@TokenMemberId Long tokenMemberId) {
        List<CartResponse> cartResponses = cartService.getCartByMemberId(tokenMemberId);
        return ResponseEntity.ok(cartResponses);
    }

    @Operation(summary = "장바구니 삭제", description = "장바구니를 삭제합니다.")
    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> removeFromCart(@PathVariable Long cartId) {
        cartService.removeFromCart(cartId);
        return ResponseEntity.noContent().build();
    }
}
