package com.travel.domain.product.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.travel.domain.accommodation.dto.response.AccommodationDetailListResponse;
import com.travel.domain.accommodation.dto.response.AccommodationImageResponse;
import com.travel.domain.accommodation.dto.response.AccommodationOptionResponse;
import com.travel.domain.map.service.KakaoMapService;
import com.travel.domain.product.dto.response.ProductDetailResponse;
import com.travel.domain.product.dto.response.ProductImageResponse;
import com.travel.domain.product.dto.response.ProductOptionResponse;
import com.travel.domain.product.dto.response.ProductResponse;
import com.travel.domain.product.service.ProductService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    ProductService productService;
    @MockBean
    KakaoMapService kakaoMapService;

    Long accommodationId = 1L;
    Long productId = 1L;
    LocalDate checkInDate = LocalDate.now();
    LocalDate checkOutDate = LocalDate.now().plusDays(1);
    int personNumber = 2;

    private AccommodationDetailListResponse createAccommodationDetailListResponse(
        AccommodationImageResponse accommodationImageResponse,
        AccommodationOptionResponse accommodationOptionResponse,
        List<ProductResponse> productResponse
    ) {
        return AccommodationDetailListResponse.builder()
            .id(accommodationId)
            .name("name")
            .description("description")
            .checkInDate(checkInDate)
            .checkOutDate(checkOutDate)
            .accommodationImage(accommodationImageResponse)
            .accommodationOption(accommodationOptionResponse)
            .productResponseList(productResponse)
            .build();
    }

    private AccommodationImageResponse createAccommodationImageResponse() {
        return AccommodationImageResponse.builder()
            .imageUrl1("image1")
            .imageUrl2("image2")
            .build();
    }

    private AccommodationOptionResponse createAccommodationOptionResponse() {
        return AccommodationOptionResponse.builder()
            .hasSmokingRoom(true)
            .hasCooking(true)
            .hasParking(true)
            .hasSwimmingPool(false)
            .hasBreakfast(true)
            .hasFitness(true)
            .hasBeauty(true)
            .build();
    }

    private ProductResponse createProductResponse(ProductImageResponse productImageResponse) {
        return ProductResponse.builder()
            .id(productId)
            .name("name")
            .checkInTime("15:00")
            .checkOutTime("11:00")
            .price(100000)
            .standardNumber(2)
            .maximumNumber(3)
            .images(productImageResponse)
            .count(2)
            .build();
    }

    private ProductImageResponse createProductImageResponse() {
        return ProductImageResponse.builder()
            .imageUrl1("img1")
            .imageUrl2("img2")
            .build();
    }

    private ProductDetailResponse createProductDetailResponse(
        ProductImageResponse productImageResponse, ProductOptionResponse productOptionResponse) {
        return ProductDetailResponse.builder()
            .id(productId)
            .name("productname")
            .accommodationName("accommodationName")
            .description("description")
            .price(100000)
            .totalPrice(200000)
            .numberOfStay(2)
            .standardNumber(2)
            .maximumNumber(3)
            .type("호텔")
            .productImageResponse(productImageResponse)
            .productOption(productOptionResponse)
            .build();
    }

    private ProductOptionResponse createProductOptionResponse() {
        return ProductOptionResponse.builder()
            .hasBath(true)
            .hasAirCondition(true)
            .hasTv(true)
            .hasPc(true)
            .hasWifi(true)
            .hasCable(true)
            .hasRefrigerator(true)
            .hasSofa(true)
            .canCook(true)
            .hasTable(true)
            .hasHairdryer(true)
            .build();
    }

    @BeforeAll
    static void setUp() {

    }

    @DisplayName("GET 숙박 디테일, 객실 요약")
    @Test
    void getAccommodationDetail() throws Exception {
        //given
        AccommodationImageResponse accommodationImageResponse = createAccommodationImageResponse();
        AccommodationOptionResponse accommodationOptionResponse = createAccommodationOptionResponse();
        ProductImageResponse productImageResponse = createProductImageResponse();
        List<ProductResponse> productResponseList = new ArrayList<>();
        ProductResponse productResponse = createProductResponse(productImageResponse);
        productResponseList.add(productResponse);
        AccommodationDetailListResponse accommodationDetailListResponse = createAccommodationDetailListResponse(
            accommodationImageResponse, accommodationOptionResponse, productResponseList
        );
        //when
        when(productService
                .getAccommodationDetail(eq(accommodationId), eq(checkInDate),
                        eq(checkOutDate), eq(personNumber)))
            .thenReturn(accommodationDetailListResponse);

        mvc.perform(get("/api/accommodations/{accommodationId}", accommodationId))
            .andExpect(status().isOk())
            .andDo(print());
        then(productService).should()
                .getAccommodationDetail(accommodationId, checkInDate, checkOutDate, personNumber);

    }

    @Test
    @DisplayName("객실 디테일 조회")
    void getProductDetail() throws Exception {
        //given
        ProductImageResponse productImageResponse = createProductImageResponse();
        ProductOptionResponse productOptionResponse = createProductOptionResponse();
        ProductDetailResponse productDetailResponse
            = createProductDetailResponse(productImageResponse, productOptionResponse);

        //when
        when(productService
                .getProductDetail(accommodationId, productId,
                        checkInDate, checkOutDate, personNumber))
            .thenReturn(productDetailResponse);

        mvc.perform(get("/api/accommodations/{accommodationId}/{productId}",
                        accommodationId, productId))
            .andExpect(status().isOk())
            .andDo(print());
        then(productService).should()
                .getProductDetail(accommodationId, productId,
                        checkInDate, checkOutDate, personNumber);
    }
}