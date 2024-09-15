package com.travel.domain.reservations.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.domain.member.entity.Member;
import com.travel.domain.member.repository.MemberRepository;
import com.travel.domain.reservations.dto.request.ReservationCancelRequest;
import com.travel.domain.reservations.dto.request.ReservationRequest;
import com.travel.domain.reservations.dto.response.ReservationCancelResponse;
import com.travel.domain.reservations.dto.response.ReservationHistoryListResponse;
import com.travel.domain.reservations.dto.response.ReservationHistoryResponse;
import com.travel.domain.reservations.dto.response.ReservationResponse;
import com.travel.domain.reservations.service.ReservationService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;

    @Autowired
    private ObjectMapper mapper;

    private final LocalDate checkInDate = LocalDate.now();

    private final LocalDate checkOutDate = LocalDate.now().plusDays(2);

    private final LocalDateTime deletedAt = LocalDateTime.of(2024, 7, 13, 19, 0, 0);

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName(value = "예약 내역 조회")
    void getReservationHistories() throws Exception {
        // Given
        Member member = createMember();

        ReservationHistoryResponse reservationResponse = createHistoryResponse();
        ReservationHistoryListResponse listResponse = createHistoryListResponse(
            reservationResponse);

        when(reservationService.getReservationHistories(member.getId()))
            .thenReturn(listResponse);

        String content = mapper.writeValueAsString(listResponse);

        // When & Then
        mockMvc.perform(get("/api/auth/reservation/history")
                .header("mock-token", member.getId()))
            .andExpect(status().isOk())
            .andExpect(content().json(content))  // JSON 응답이 비어있지 않음을 확인
            .andDo(print());
        then(reservationService).should().getReservationHistories(member.getId());
    }

    @Test
    @DisplayName(value = "예약")
    void reservation() throws Exception {
        // Given
        Member member = createMember();

        ReservationRequest reservationRequest = createReservationRequest();
        ReservationResponse response = createReservationResponse();

        when(reservationService.createReservation(any(ReservationRequest.class),
            eq(member.getId())))
            .thenReturn(response);

        String content = mapper.writeValueAsString(response);

        // When & Then
        mockMvc.perform(post("/api/auth/reservation")
                .header("mock-token", member.getId())
                .content(mapper.writeValueAsString(reservationRequest))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(content))  // JSON 응답이 비어있지 않음을 확인
            .andDo(print());
    }

    @Test
    @DisplayName(value = "예약 취소")
    void reservationCancel() throws Exception {
        Member member = createMember();
        ReservationCancelRequest request = createCancelRequest();
        ReservationCancelResponse response = createCancelResponse();

        when(reservationService.cancelReservation(any(), any(ReservationCancelRequest.class)))
            .thenReturn(response);

        String content = mapper.writeValueAsString(response);

        int price = 150000;
        int totalPrice = calcNight() * price;

        // When & Then
        mockMvc.perform(delete("/api/auth/reservation")
                .header("mock-token", member.getId())
                .content(mapper.writeValueAsString(request))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(content))  // JSON 응답이 비어있지 않음을 확인
            .andDo(print());
    }

    private Member createMember() {
        return Member
            .builder()
            .id(1L)
            .email("test@gmail.com")
            .name("testMember")
            .password("testMember1@#$")
            .build();
    }

    private ReservationHistoryResponse createHistoryResponse() {
        return ReservationHistoryResponse
            .builder()
            .id(99L)
            .checkInDate(checkInDate)
            .checkOutDate(checkOutDate)
            .night(2)
            .personNumber(2)
            .accommodationName("테스트 호텔")
            .roomType("VIP")
            .standardNumber(2)
            .maximumNumber(4)
            .imageUrl("ImageUrl")
            .price(400000)
            .totalPrice(800000)
            .build();
    }

    private ReservationHistoryListResponse createHistoryListResponse(
        ReservationHistoryResponse reservationResponse) {
        return ReservationHistoryListResponse
            .builder()
            .reservationHistoryList(List.of(reservationResponse))
            .build();
    }

    private ReservationResponse createReservationResponse() {
        return ReservationResponse
            .builder()
            .id(66L)
            .personNumber(2)
            .totalPrice(600000)
            .price(300000)
            .checkInDate(checkInDate)
            .checkOutDate(checkOutDate)
            .build();
    }

    private ReservationRequest createReservationRequest() {
        return new ReservationRequest(1L, 3L, checkInDate, checkOutDate, 2);
    }

    private ReservationCancelRequest createCancelRequest() {
        return new ReservationCancelRequest(1L);
    }

    private ReservationCancelResponse createCancelResponse() {
        return ReservationCancelResponse
            .builder()
            .id(1L)
            .checkInDate(checkInDate)
            .checkOutDate(checkOutDate)
            .night(2)
            .personNumber(2)
            .accommodationName("Test Accommodation")
            .roomType("VIP")
            .standardNumber(2)
            .maximumNumber(4)
            .price(150000)
            .totalPrice(300000)
            .deletedAt(deletedAt)
            .build();
    }

    private int calcNight() {
        return (int) ChronoUnit.DAYS.between(checkInDate, checkOutDate);
    }

}