package com.travel.domain.product.dto.response;


import com.travel.domain.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class ProductDetailResponse {

    private Long id;

    private String name;

    private String accommodationName;

    private String description;

    private int totalPrice;

    private int price;

    private int numberOfStay;

    private int standardNumber;

    private int maximumNumber;

    private String type;

    private ProductImageResponse productImageResponse;

    private ProductOptionResponse productOption;


    public static ProductDetailResponse from(Product product, String accommodationName,
        int price, int totalPrice,
        int numberOfStay, ProductImageResponse productImageResponse,
        ProductOptionResponse productOptionResponse) {
        return ProductDetailResponse.builder()
            .id(product.getId())
            .name(product.getName())
            .accommodationName(accommodationName)
            .description(product.getDescription())
            .price(price)
            .totalPrice(totalPrice)
            .numberOfStay(numberOfStay)
            .standardNumber(product.getStandardNumber())
            .maximumNumber(product.getMaximumNumber())
            .type(product.getType())
            .productImageResponse(productImageResponse)
            .productOption(productOptionResponse)
            .build();
    }
}