package com.travel.domain.cart.dto.request;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CartRequest {

    private Long accommodationId;

    private Long productId;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private Integer personNumber;
}
