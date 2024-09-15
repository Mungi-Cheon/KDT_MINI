package com.travel.domain.reservations.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class ReservationHistoryListResponse {

    private List<ReservationHistoryResponse> reservationHistoryList;
}
