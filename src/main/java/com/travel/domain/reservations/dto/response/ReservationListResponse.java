package com.travel.domain.reservations.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class ReservationListResponse {

    private List<ReservationResponse> reservationResponseList;

    public static ReservationListResponse from(List<ReservationResponse> list) {
        return ReservationListResponse
            .builder()
            .reservationResponseList(list)
            .build();
    }
}
