package com.travel.domain.product.dto.response;


import com.travel.domain.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class ProductResponse {

    private Long id;

    private String name;

    private String checkInTime;

    private String checkOutTime;

    private int price;

    private int standardNumber;

    private int maximumNumber;

    private ProductImageResponse images;

    private int count;

    public static ProductResponse from(
        Product product, int count,
        ProductImageResponse productImageResponse) {
        return ProductResponse.builder()
            .id(product.getId())
            .name(product.getName())
            .checkInTime(product.getCheckInTime())
            .checkOutTime(product.getCheckOutTime())
            .price(product.getProductInfoPerNightsList().get(0).getPrice())
            .standardNumber(product.getStandardNumber())
            .maximumNumber(product.getMaximumNumber())
            .images(productImageResponse)
            .count(count)
            .build();
    }
}
