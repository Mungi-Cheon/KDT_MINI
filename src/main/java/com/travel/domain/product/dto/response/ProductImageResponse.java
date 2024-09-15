package com.travel.domain.product.dto.response;

import com.travel.domain.product.entity.ProductImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ProductImageResponse {

    private String imageUrl1;

    private String imageUrl2;

    public static ProductImageResponse from(ProductImage productImage) {
        return ProductImageResponse.builder()
            .imageUrl1(productImage.getImageUrl1())
            .imageUrl2(productImage.getImageUrl2())
            .build();
    }
}