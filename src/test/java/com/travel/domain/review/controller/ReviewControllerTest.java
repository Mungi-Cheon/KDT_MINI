package com.travel.domain.review.controller;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.travel.domain.accommodation.dto.response.AccommodationDetailListResponse;
import com.travel.domain.accommodation.dto.response.AccommodationImageResponse;
import com.travel.domain.accommodation.dto.response.AccommodationOptionResponse;
import com.travel.domain.accommodation.entity.Accommodation;
import com.travel.domain.accommodation.entity.AccommodationImage;
import com.travel.domain.accommodation.entity.AccommodationOption;
import com.travel.domain.product.dto.response.ProductDetailResponse;
import com.travel.domain.product.dto.response.ProductImageResponse;
import com.travel.domain.product.dto.response.ProductOptionResponse;
import com.travel.domain.product.dto.response.ProductResponse;
import com.travel.domain.product.entity.Product;
import com.travel.domain.product.entity.ProductImage;
import com.travel.domain.product.entity.ProductInfoPerNight;
import com.travel.domain.product.entity.ProductOption;
import com.travel.domain.review.dto.response.AccommodationReviewResponseList;
import com.travel.domain.review.dto.response.ReviewResponse;
import com.travel.domain.review.service.ReviewService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
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
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    Long id = 1L;
    LocalDate checkInDate = LocalDate.now().minusDays(3);
    LocalDate checkOutDate = checkInDate.plus(Period.ofDays(2));
    String checkInTime = "15:00";
    String checkOutTime = "11:00";

    @BeforeEach
    public void setUp() {
    }

    @Test
    @DisplayName("숙박 id 별 리뷰 조회")
    void getReviewList() throws Exception {
        Product product = new Product();
        Accommodation accommodation = new Accommodation();
        AccommodationImage accommodationImage = createAccommodationImage(accommodation);
        AccommodationOption accommodationOption = createAccommodationOption(accommodation);
        ProductImage productImage = createProductImage(product, accommodation);
        ProductOption productOption = createProductOption(product,accommodation);
        ProductInfoPerNight productInfoPerNight1 = createProductInfoPerNight(product, accommodation, checkInDate);
        ProductInfoPerNight productInfoPerNight2 = createProductInfoPerNight(product, accommodation, checkOutDate);
        List<ProductInfoPerNight> productInfoPerNightList = new ArrayList<>();
        productInfoPerNightList.add(productInfoPerNight1);
        productInfoPerNightList.add(productInfoPerNight2);

        product = createProduct(accommodation, productImage, productInfoPerNightList,
                productOption);
        List<Product> productList = List.of(product);
        accommodation  =createAccommodation(accommodationOption, accommodationImage, productList);

        ReviewResponse reviewResponse = createReviewResponse();
        List<ReviewResponse> reviewResponseList= List.of(reviewResponse);
        AccommodationReviewResponseList accommodationReviewResponseList = createAccommodationReviewResponseList(accommodation, reviewResponseList);

        when(reviewService.getReviewList(id))
                .thenReturn(accommodationReviewResponseList);

        mockMvc.perform(get("/api/review/{accommodationId}", id))
                .andExpect(status().isOk())
                .andDo(print());
        then(reviewService).should().getReviewList(id);
    }

    private ReviewResponse createReviewResponse() {
        return ReviewResponse.builder()
                .id(id)
                .accommodationName("accommodationName")
                .productName("productName")
                .memberName("memberName")
                .reservationId(id)
                .title("title")
                .comment("comment")
                .grade(BigDecimal.valueOf(4.1))
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .build();
    }

    private AccommodationReviewResponseList createAccommodationReviewResponseList(
            Accommodation accommodation, List<ReviewResponse> reviewResponseList) {
        String thumbnail = accommodation.getImages().getThumbnail();
        return AccommodationReviewResponseList.builder()
                .id(id)
                .name(accommodation.getName())
                .grade(accommodation.getGrade())
                .thumbnail(thumbnail)
                .reviewResponseList(reviewResponseList)
                .build();
    }

    private Accommodation createAccommodation(
            AccommodationOption accommodationOption, AccommodationImage accommodationImage,
            List<Product> productList) {
        return Accommodation.builder()
                .id(id)
                .name("accommodationName")
                .contact("064-729-8100")
                .description("accommodation description")
                .address("제주특별자치도 제주시 탑동로 66")
                .category("호텔")
                .grade(BigDecimal.valueOf(4.1))
                .options(accommodationOption)
                .images(accommodationImage)
                .products(productList)
                .build();
    }
    private AccommodationOption createAccommodationOption(Accommodation accommodation) {
        return AccommodationOption.builder()
                .id(id)
                .accommodation(accommodation)
                .hasSmokingRoom(true)
                .hasCooking(true)
                .hasParking(true)
                .hasSwimmingPool(false)
                .hasBreakfast(true)
                .hasFitness(true)
                .hasBeauty(true)
                .build();
    }

    private AccommodationImage createAccommodationImage(Accommodation accommodation) {
        return AccommodationImage.builder()
                .id(id)
                .accommodation(accommodation)
                .thumbnail("thumnail")
                .imageUrl1("img1")
                .imageUrl2("img2")
                .build();
    }
    private Product createProduct(
            Accommodation accommodation, ProductImage productImage,
            List<ProductInfoPerNight> productInfoPerNightList, ProductOption productOption) {
        return Product.builder()
                .id(id)
                .accommodation(accommodation)
                .name("ProductName")
                .description("ProductDescription")
                .checkInTime(checkInTime)
                .checkOutTime(checkOutTime)
                .standardNumber(2)
                .maximumNumber(3)
                .type("Standard")
                .productOption(productOption)
                .productInfoPerNightsList(productInfoPerNightList)
                .productImage(productImage)
                .build();
    }

    private ProductOption createProductOption(Product product, Accommodation accommodation) {
        return ProductOption.builder()
                .id(1L)
                .product(product)
                .accommodation(accommodation)
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

    private ProductImage createProductImage(Product product, Accommodation accommodation) {
        return ProductImage.builder()
                .id(id)
                .product(product)
                .imageUrl1("img1")
                .imageUrl2("img2")
                .accommodation(accommodation)
                .build();
    }

    private ProductInfoPerNight createProductInfoPerNight(Product product,
            Accommodation accommodation, LocalDate date) {
        return ProductInfoPerNight.builder()
                .id(id)
                .product(product)
                .accommodation(accommodation)
                .date(date)
                .price(100000)
                .count(2)
                .build();
    }

    private AccommodationDetailListResponse createAccommodationDetailListResponse(
            AccommodationImageResponse accommodationImageResponse,
            AccommodationOptionResponse accommodationOptionResponse,
            List<ProductResponse> productResponse
    ) {
        return AccommodationDetailListResponse.builder()
                .id(id)
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
                .id(id)
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
            ProductImageResponse productImageResponse,
            ProductOptionResponse productOptionResponse) {
        return ProductDetailResponse.builder()
                .id(id)
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
}