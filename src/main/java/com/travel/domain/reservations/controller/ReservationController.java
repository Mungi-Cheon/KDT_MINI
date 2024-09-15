package com.travel.domain.reservations.controller;

import com.travel.domain.reservations.dto.request.ReservationCancelRequest;
import com.travel.domain.reservations.dto.request.ReservationRequest;
import com.travel.domain.reservations.dto.response.ReservationCancelResponse;
import com.travel.domain.reservations.dto.response.ReservationHistoryListResponse;
import com.travel.domain.reservations.dto.response.ReservationResponse;
import com.travel.domain.reservations.service.ReservationService;
import com.travel.global.annotation.TokenMemberId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Reservation API", description = "예약 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class ReservationController {

    private final ReservationService reservationService;

    @Operation(summary = "예약 내역 조회", description = "숙소 예약 내역을 조회합니다.")
    @ApiResponse(content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = ReservationHistoryListResponse.class)))
    @GetMapping("/reservation/history")
    public ResponseEntity<ReservationHistoryListResponse> getReservationHistories(
        @TokenMemberId Long tokenMemberId) {
        ReservationHistoryListResponse response = reservationService
            .getReservationHistories(tokenMemberId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "숙소 예약", description = "숙소를 예약합니다.")
    @ApiResponse(content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = ReservationResponse.class)))
    @PostMapping("/reservation")
    public ResponseEntity<ReservationResponse> reservation(
        @TokenMemberId Long tokenMemberId,
        @Valid @RequestBody ReservationRequest request) {
        ReservationResponse response = reservationService
            .createReservation(request, tokenMemberId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/reservation")
    public ResponseEntity<ReservationCancelResponse> reservationCancel(
        @TokenMemberId Long tokenMemberId, @RequestBody ReservationCancelRequest request) {
        ReservationCancelResponse response = reservationService
            .cancelReservation(tokenMemberId, request);
        return ResponseEntity.ok(response);
    }
}