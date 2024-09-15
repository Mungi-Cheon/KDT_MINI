package com.travel.domain.reservations.dto.response;

import com.travel.domain.reservations.entity.Reservation;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class ReservationCancelResponse {

    private Long id;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private Integer night;

    private Integer personNumber;

    private String accommodationName;

    private String roomType;

    private Integer standardNumber;

    private Integer maximumNumber;

    private Integer price;

    private Integer totalPrice;

    private LocalDateTime deletedAt;

    public static ReservationCancelResponse from(Reservation entity, String accommodationName
        , String roomType, Integer standardNumber, Integer maximumNumber, LocalDateTime deletedAt) {
        return ReservationCancelResponse.builder()
            .id(entity.getId())
            .checkInDate(entity.getCheckInDate())
            .checkOutDate(entity.getCheckOutDate())
            .night(entity.getNight())
            .personNumber(entity.getPersonNumber())
            .accommodationName(accommodationName)
            .roomType(roomType)
            .standardNumber(standardNumber)
            .maximumNumber(maximumNumber)
            .price(entity.getPrice())
            .totalPrice(calcTotalPrice(entity.getPrice(), entity.getNight()))
            .deletedAt(deletedAt)
            .build();
    }

    private static int calcTotalPrice(int price, int night) {
        return price * night;
    }
}
