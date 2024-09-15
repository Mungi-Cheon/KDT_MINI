package com.travel.domain.product.dto.response;

import com.travel.domain.product.entity.ProductOption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ProductOptionResponse {

    private boolean hasBath;

    private boolean hasAirCondition;

    private boolean hasTv;

    private boolean hasPc;

    private boolean hasWifi;

    private boolean hasCable;

    private boolean hasRefrigerator;

    private boolean hasSofa;

    private boolean canCook;

    private boolean hasTable;

    private boolean hasHairdryer;

    public static ProductOptionResponse from(ProductOption productOption) {
        return ProductOptionResponse.builder()
            .hasBath(productOption.getHasBath())
            .hasAirCondition(productOption.getHasAirCondition())
            .hasTv(productOption.getHasTv())
            .hasPc(productOption.getHasPc())
            .hasWifi(productOption.getHasWifi())
            .hasCable(productOption.getHasCable())
            .hasRefrigerator(productOption.getHasRefrigerator())
            .hasSofa(productOption.getHasSofa())
            .canCook(productOption.getCanCook())
            .hasTable(productOption.getHasTable())
            .hasHairdryer(productOption.getHasHairdryer())
            .build();
    }
}