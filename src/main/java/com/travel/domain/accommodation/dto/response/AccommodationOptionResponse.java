package com.travel.domain.accommodation.dto.response;

import com.travel.domain.accommodation.entity.AccommodationOption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class AccommodationOptionResponse {

    private boolean hasSmokingRoom;

    private boolean hasCooking;

    private boolean hasParking;

    private boolean hasSwimmingPool;

    private boolean hasBreakfast;

    private boolean hasFitness;

    private boolean hasBeauty;

    public static AccommodationOptionResponse from(AccommodationOption accommodationOption) {
        return AccommodationOptionResponse.builder()
            .hasSmokingRoom(accommodationOption.getHasSmokingRoom())
            .hasCooking(accommodationOption.getHasCooking())
            .hasParking(accommodationOption.getHasParking())
            .hasSwimmingPool(accommodationOption.getHasSwimmingPool())
            .hasBreakfast(accommodationOption.getHasBreakfast())
            .hasFitness(accommodationOption.getHasFitness())
            .hasBeauty(accommodationOption.getHasBeauty())
            .build();
    }
}
