package com.travel.domain.reservations.dto.response;

import com.travel.domain.reservations.entity.Reservation;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class ReservationHistoryResponse {

    private Long id;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private Integer night;

    private Integer personNumber;

    private Long accommodationId;

    private String accommodationName;

    private String roomType;

    private Integer standardNumber;

    private Integer maximumNumber;

    private String imageUrl;

    private Integer price;

    private Integer totalPrice;

    public static ReservationHistoryResponse from(Reservation entity, Long accommodationId,
        String accommodationName, String roomType, Integer standardNumber,
        Integer maximumNumber, String imageUrl) {
        return ReservationHistoryResponse.builder()
            .id(entity.getId())
            .checkInDate(entity.getCheckInDate())
            .checkOutDate(entity.getCheckOutDate())
            .night(entity.getNight())
            .personNumber(entity.getPersonNumber())
            .accommodationId(accommodationId)
            .accommodationName(accommodationName)
            .roomType(roomType)
            .standardNumber(standardNumber)
            .maximumNumber(maximumNumber)
            .imageUrl(imageUrl)
            .price(entity.getPrice())
            .totalPrice(calcTotalPrice(entity.getPrice(), entity.getNight()))
            .build();
    }

    private static int calcTotalPrice(int price, int night) {
        return price * night;
    }
}
