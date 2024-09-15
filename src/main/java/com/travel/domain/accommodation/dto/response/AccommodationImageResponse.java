package com.travel.domain.accommodation.dto.response;


import com.travel.domain.accommodation.entity.AccommodationImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class AccommodationImageResponse {

    private String imageUrl1;

    private String imageUrl2;

    public static AccommodationImageResponse from(AccommodationImage accommodationImage) {
        return AccommodationImageResponse.builder()
            .imageUrl1(accommodationImage.getImageUrl1())
            .imageUrl2(accommodationImage.getImageUrl2())
            .build();
    }
}