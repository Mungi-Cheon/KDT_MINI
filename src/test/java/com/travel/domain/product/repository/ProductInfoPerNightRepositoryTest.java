package com.travel.domain.product.repository;

import com.travel.domain.accommodation.entity.Accommodation;
import com.travel.domain.accommodation.entity.AccommodationImage;
import com.travel.domain.accommodation.entity.AccommodationOption;
import com.travel.domain.product.entity.Product;
import com.travel.domain.product.entity.ProductImage;
import com.travel.domain.product.entity.ProductInfoPerNight;
import com.travel.domain.product.entity.ProductOption;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class ProductInfoPerNightRepositoryTest {

    @Mock
    ProductInfoPerNightRepository productInfoPerNightRepository;

    Long accommodationId = 1L;
    Long productId = 1L;
    LocalDate checkInDate = LocalDate.now();
    LocalDate checkOutDate = LocalDate.now().plusDays(2);
    Long productImageId = 1L;
    Long productInfoPerNightId = 1L;
    Long accommodationOptionId = 1L;
    Long accommodationImageId = 1L;
    LocalDate infoDate = checkInDate.plusDays(1);
    String checkInTime = "15:00";
    String checkOutTime = "11:00";
    int standardNumber = 2;
    int maximumNumber = 3;

    private Accommodation createAccommodation(
        AccommodationOption accommodationOption, AccommodationImage accommodationImage,
        List<Product> productList) {
        return Accommodation.builder()
            .id(accommodationId)
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
            .id(accommodationOptionId)
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
            .id(accommodationImageId)
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
            .id(productId)
            .accommodation(accommodation)
            .name("ProductName")
            .description("ProductDescription")
            .checkInTime(checkInTime)
            .checkOutTime(checkOutTime)
            .standardNumber(standardNumber)
            .maximumNumber(maximumNumber)
            .type("Standard")
            .productOption(productOption)
            .productInfoPerNightsList(productInfoPerNightList)
            .productImage(productImage)
            .build();

    }

    private ProductOption createProductOption() {
        return ProductOption.builder()
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
            .id(productImageId)
            .product(product)
            .imageUrl1("img1")
            .imageUrl2("img2")
            .accommodation(accommodation)
            .build();
    }

    private ProductInfoPerNight createProductInfoPerNight(Product product, Accommodation accommodation) {
        return ProductInfoPerNight.builder()
            .id(productInfoPerNightId)
            .product(product)
            .accommodation(accommodation)
            .date(infoDate)
            .price(400000)
            .count(2)
            .build();
    }

    @BeforeAll
    static void setUp() {
    }

    @Test
    @DisplayName("productId에 대한 날자 범위 내에 있는 상품 가격 정보 조회")
    void findByProductIdAndDateRange() {
        Accommodation accommodation = new Accommodation();
        AccommodationOption accommodationOption = createAccommodationOption(accommodation);
        AccommodationImage accommodationImage = createAccommodationImage(accommodation);
        Product product = new Product();
        List<Product> productList = new ArrayList<>();
        List<ProductInfoPerNight> productInfoPerNightList = new ArrayList<>();
        accommodation = createAccommodation(accommodationOption, accommodationImage, productList);
        ProductOption productOption = createProductOption();
        ProductImage productImage = createProductImage(product, accommodation);
        ProductInfoPerNight productInfoPerNight = createProductInfoPerNight(product, accommodation);
        productInfoPerNightList.add(productInfoPerNight);
        product = createProduct(accommodation, productImage, productInfoPerNightList, productOption);
        productList.add(product);

        lenient().when(productInfoPerNightRepository
                .findByProductIdAndDateRange(eq(productId), eq(checkInDate), eq(checkOutDate)))
            .thenReturn(productInfoPerNightList);


        List<ProductInfoPerNight> result = productInfoPerNightRepository
            .findByProductIdAndDateRange(productId, checkInDate, checkOutDate);

        assertEquals(productList.size(), result.size());
        assertEquals(productInfoPerNightList.get(0), result.get(0));
    }

    @Test
    @DisplayName("productId와 date에 해당하는 상품 정보가 존재하는지 여부를 확인")
    void existsByProductIdAndDate() {
        Accommodation accommodation = new Accommodation();
        AccommodationOption accommodationOption = createAccommodationOption(accommodation);
        AccommodationImage accommodationImage = createAccommodationImage(accommodation);
        Product product = new Product();
        List<Product> productList = new ArrayList<>();
        List<ProductInfoPerNight> productInfoPerNightList = new ArrayList<>();
        accommodation = createAccommodation(accommodationOption, accommodationImage, productList);
        ProductOption productOption = createProductOption();
        ProductImage productImage = createProductImage(product, accommodation);
        ProductInfoPerNight productInfoPerNight = createProductInfoPerNight(product, accommodation);
        productInfoPerNightList.add(productInfoPerNight);
        product = createProduct(accommodation, productImage, productInfoPerNightList, productOption);
        productList.add(product);

        for (LocalDate date = checkInDate; date.isBefore(checkOutDate); date = date.plusDays(1)) {
            when(productInfoPerNightRepository
                .existsByProductIdAndDate(eq(productId), eq(date)))
                .thenReturn(true);

            boolean exists = productInfoPerNightRepository.existsByProductIdAndDate(productId, date);

            assertTrue(exists);
        }
    }

    @Test
    @DisplayName("checkIndate 와 checkOutDate 내에서 최소 count 값을 조회")
    void findMinCountByProductIdAndDateRange() {
        Accommodation accommodation = new Accommodation();
        AccommodationOption accommodationOption = createAccommodationOption(accommodation);
        AccommodationImage accommodationImage = createAccommodationImage(accommodation);
        Product product = new Product();
        List<Product> productList = new ArrayList<>();
        List<ProductInfoPerNight> productInfoPerNightList = new ArrayList<>();
        accommodation = createAccommodation(accommodationOption, accommodationImage, productList);
        ProductOption productOption = createProductOption();
        ProductImage productImage = createProductImage(product, accommodation);
        ProductInfoPerNight productInfoPerNight = createProductInfoPerNight(product, accommodation);
        productInfoPerNightList.add(productInfoPerNight);
        product = createProduct(accommodation, productImage, productInfoPerNightList, productOption);
        productList.add(product);

        lenient().when(productInfoPerNightRepository
                .findMinCountByProductIdAndDateRange(eq(productId), eq(checkInDate), eq(checkOutDate)))
            .thenReturn(2);

        Integer minCount = productInfoPerNightRepository
            .findMinCountByProductIdAndDateRange(productId, checkInDate, checkOutDate);

        assertEquals(2, minCount);
    }

    @Test
    @DisplayName("숙소 ID와 날짜 범위로 ProductInfoPerNight 조회")
    void findByAccommodationIdAndDateRange() {
        Accommodation accommodation = new Accommodation();
        AccommodationOption accommodationOption = createAccommodationOption(accommodation);
        AccommodationImage accommodationImage = createAccommodationImage(accommodation);
        Product product = new Product();
        List<Product> productList = new ArrayList<>();
        List<ProductInfoPerNight> productInfoPerNightList = new ArrayList<>();
        accommodation = createAccommodation(accommodationOption, accommodationImage, productList);
        ProductOption productOption = createProductOption();
        ProductImage productImage = createProductImage(product, accommodation);
        ProductInfoPerNight productInfoPerNight = createProductInfoPerNight(product, accommodation);
        productInfoPerNightList.add(productInfoPerNight);
        product = createProduct(accommodation, productImage, productInfoPerNightList, productOption);
        productList.add(product);

        when(productInfoPerNightRepository
            .findByAccommodationIdAndDateRange(accommodationId, checkInDate, checkOutDate))
            .thenReturn(productInfoPerNightList);

        List<ProductInfoPerNight> result = productInfoPerNightRepository
            .findByAccommodationIdAndDateRange(accommodationId, checkInDate, checkOutDate);

        assertEquals(1, result.size());
        assertEquals(productInfoPerNight, result.get(0));
    }

    @Test
    @DisplayName("productId와 날짜 범위에 해당하는 ProductInfoPerNight 엔티 목록 반환")
    void findByProductIdAndDateRangeWithPessimisticLock() {
        Accommodation accommodation = new Accommodation();
        AccommodationOption accommodationOption = createAccommodationOption(accommodation);
        AccommodationImage accommodationImage = createAccommodationImage(accommodation);
        Product product = new Product();
        List<Product> productList = new ArrayList<>();
        List<ProductInfoPerNight> productInfoPerNightList = new ArrayList<>();
        accommodation = createAccommodation(accommodationOption, accommodationImage, productList);
        ProductOption productOption = createProductOption();
        ProductImage productImage = createProductImage(product, accommodation);
        ProductInfoPerNight productInfoPerNight = createProductInfoPerNight(product, accommodation);
        productInfoPerNightList.add(productInfoPerNight);
        product = createProduct(accommodation, productImage, productInfoPerNightList, productOption);
        productList.add(product);

        when(productInfoPerNightRepository.findByProductIdAndDateRangeWithPessimisticLock(productId, checkInDate, checkOutDate))
            .thenReturn(productInfoPerNightList);

        List<ProductInfoPerNight> result = productInfoPerNightRepository
            .findByProductIdAndDateRangeWithPessimisticLock(productId, checkInDate, checkOutDate);

        assertEquals(productInfoPerNightList.size(), result.size());
        assertEquals(productInfoPerNightList.get(0).getId(),result.get(0).getId());
    }

}