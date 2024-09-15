package com.travel.domain.product.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.travel.domain.accommodation.entity.Accommodation;
import com.travel.domain.accommodation.entity.AccommodationImage;
import com.travel.domain.accommodation.entity.AccommodationOption;
import com.travel.domain.product.entity.Product;
import com.travel.domain.product.entity.ProductImage;
import com.travel.domain.product.entity.ProductInfoPerNight;
import com.travel.domain.product.entity.ProductOption;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;


@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class ProductRepositoryTest {

    @Mock
    private ProductRepository productRepository;

    Long accommodationId = 1L;
    Long productId = 1L;
    LocalDate checkInDate = LocalDate.now();
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

    private ProductInfoPerNight createProductInfoPerNight(Product product,
        Accommodation accommodation) {
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
    @DisplayName("숙소 id로 상품 조회")
    void findAllByAccommodationId() {
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
        product = createProduct(accommodation, productImage, productInfoPerNightList,
            productOption);
        productList.add(product);

        when(productRepository.findAllByAccommodationId(accommodationId)).thenReturn(productList);
        List<Product> result = productRepository.findAllByAccommodationId(accommodationId);

        assertEquals(productList.size(), result.size());
        assertEquals(productList.get(0).getId(), result.get(0).getId());
        assertEquals(productList.get(0).getName(), result.get(0).getName());
    }

    @Test
    @DisplayName("productId, accommodationId ")
    void findByIdAndAccommodationId() {
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
        product = createProduct(accommodation, productImage, productInfoPerNightList,
            productOption);
        productList.add(product);

        productRepository.save(product);

        when(productRepository.findByIdAndAccommodationId(productId, accommodationId))
            .thenReturn(Optional.of(product));

        Optional<Product> result = productRepository.findByIdAndAccommodationId(productId,
            accommodationId);

        assertTrue(result.isPresent());
        assertEquals(productId, result.get().getId());
        assertEquals(accommodationId, result.get().getAccommodation().getId());
    }

    @Test
    @DisplayName("비관적 락 - 객실 id로 객실 찾기")
    void findByIdWithPessimisticLock() {
        Product product = new Product();
        Product.builder()
            .id(productId)
            .build();

        when(productRepository.findByAccommodationIdAndProductIdJoinImagesAndOption(accommodationId,productId))
            .thenReturn(Optional.of(product));

        Optional<Product> result =
            productRepository.findByAccommodationIdAndProductIdJoinImagesAndOption(accommodationId,productId);

        assertTrue(result.isPresent());
        assertEquals(product, result.get());
        verify(productRepository, times(1)).findByAccommodationIdAndProductIdJoinImagesAndOption(accommodationId,productId);
    }

    @Test
    @DisplayName("패치 조인을 사용한 accommodationId 로 product 찾기")
    void findAllByAccommodationIdWithFetchJoin() {
        Accommodation accommodation = Accommodation.builder()
            .id(accommodationId)
            .build();

        Product product = Product.builder()
            .id(productId)
            .accommodation(accommodation)
            .build();
        List<Product> productList = new ArrayList<>();
        productList.add(product);

        lenient().when(productRepository.findAllByAccommodationIdWithFetchJoin(accommodationId))
            .thenReturn(productList);

        List<Product> result = productRepository.findAllByAccommodationIdWithFetchJoin(
            accommodationId);

        assertNotNull(result);
        assertEquals(accommodationId, result.get(0).getAccommodation().getId());
    }
}