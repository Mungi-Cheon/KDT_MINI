package com.travel.domain.accommodation.dto.response;


import com.travel.domain.accommodation.entity.Accommodation;
import com.travel.domain.map.dto.response.DocumentResponse;
import com.travel.domain.map.dto.response.MapResponse;
import com.travel.domain.product.dto.response.ProductResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class AccommodationDetailListResponse {

    private Long id;

    private String name;

    private String description;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private AccommodationImageResponse accommodationImage;

    private AccommodationOptionResponse accommodationOption;

    private List<ProductResponse> productResponseList;

    private Boolean liked;

    private int likeCount;

    private String category;

    private BigDecimal grade;

    private double longitude;

    private double latitude;

    private String addressName;

    public static AccommodationDetailListResponse from(Accommodation accommodation,
        LocalDate checkInDate,
        LocalDate checkOutDate, AccommodationImageResponse accommodationImage,
        AccommodationOptionResponse accommodationOption,
        List<ProductResponse> productResponseList, Boolean liked, int likeCount,
        MapResponse mapResponse) {
        return AccommodationDetailListResponse.builder()
            .id(accommodation.getId())
            .name(accommodation.getName())
            .description(accommodation.getDescription())
            .checkInDate(checkInDate)
            .checkOutDate(checkOutDate)
            .accommodationImage(accommodationImage)
            .accommodationOption(accommodationOption)
            .productResponseList(productResponseList)
            .liked(liked)
            .likeCount(likeCount)
            .category(accommodation.getCategory())
            .grade(accommodation.getGrade())
            .latitude(mapResponse.getDocuments().get(0).getLatitude())
            .longitude(mapResponse.getDocuments().get(0).getLongitude())
            .addressName(mapResponse.getDocuments().get(0).getAddressName())
            .build();
    }
}