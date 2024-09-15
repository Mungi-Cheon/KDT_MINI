package com.travel.domain.product.dto.response;


import com.travel.domain.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class ProductSimpleResponse {

    private Long id;

    private String name;

    private String checkInTime;

    private String checkOutTime;

    private int standardNumber;

    private int maximumNumber;

    private String image;

    public static ProductSimpleResponse from(Product product) {
        String imgUrl = product.getProductImage().getImageUrl1();
        return ProductSimpleResponse.builder()
            .id(product.getId())
            .name(product.getName())
            .checkInTime(product.getCheckInTime())
            .checkOutTime(product.getCheckOutTime())
            .standardNumber(product.getStandardNumber())
            .maximumNumber(product.getMaximumNumber())
            .image(imgUrl)
            .build();
    }
}
