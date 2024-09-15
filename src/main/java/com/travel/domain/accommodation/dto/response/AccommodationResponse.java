package com.travel.domain.accommodation.dto.response;

import com.travel.domain.accommodation.entity.Accommodation;
import com.travel.domain.product.entity.ProductInfoPerNight;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AccommodationResponse {

    private Long id;

    private String name;

    private Integer price;

    private String thumbnail;

    private String category;

    private BigDecimal grade;

    public static AccommodationResponse from(Accommodation accommodation, int price) {
        String thumbnail = accommodation.getImages().getThumbnail();

        return AccommodationResponse.builder()
            .id(accommodation.getId())
            .name(accommodation.getName())
            .price(price)
            .thumbnail(thumbnail)
            .category(accommodation.getCategory())
            .grade(accommodation.getGrade())
            .build();
    }

    public static AccommodationResponse createAccommodationResponse(Accommodation accommodation) {
        int price = accommodation.getProducts().stream()
            .filter(product -> "standard".equalsIgnoreCase(product.getType()))
            .flatMap(product -> product.getProductInfoPerNightsList().stream())
            .mapToInt(ProductInfoPerNight::getPrice)
            .findFirst()
            .orElse(0);

        return AccommodationResponse.from(accommodation, price);
    }
}