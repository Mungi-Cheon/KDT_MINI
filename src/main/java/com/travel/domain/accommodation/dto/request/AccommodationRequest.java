package com.travel.domain.accommodation.dto.request;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springdoc.core.annotations.ParameterObject;

@Getter
@AllArgsConstructor
@ParameterObject
public class AccommodationRequest {

    private Integer categoryId;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private Integer personNumber;
}
