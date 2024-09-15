package com.travel.domain.review.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import com.travel.domain.accommodation.dto.response.AccommodationDetailListResponse;
import com.travel.domain.accommodation.dto.response.AccommodationImageResponse;
import com.travel.domain.accommodation.dto.response.AccommodationOptionResponse;
import com.travel.domain.accommodation.entity.Accommodation;
import com.travel.domain.accommodation.entity.AccommodationImage;
import com.travel.domain.accommodation.entity.AccommodationOption;
import com.travel.domain.accommodation.repository.AccommodationRepository;
import com.travel.domain.member.entity.Member;
import com.travel.domain.member.repository.MemberRepository;
import com.travel.domain.product.dto.response.ProductDetailResponse;
import com.travel.domain.product.dto.response.ProductImageResponse;
import com.travel.domain.product.dto.response.ProductOptionResponse;
import com.travel.domain.product.dto.response.ProductResponse;
import com.travel.domain.product.entity.Product;
import com.travel.domain.product.entity.ProductImage;
import com.travel.domain.product.entity.ProductInfoPerNight;
import com.travel.domain.product.entity.ProductOption;
import com.travel.domain.product.repository.ProductRepository;
import com.travel.domain.reservations.entity.Reservation;
import com.travel.domain.reservations.repository.ReservationRepository;
import com.travel.domain.review.dto.request.ReviewRequest;
import com.travel.domain.review.dto.response.AccommodationReviewResponseList;
import com.travel.domain.review.dto.response.DeleteReviewResponse;
import com.travel.domain.review.dto.response.ReviewResponse;
import com.travel.domain.review.dto.response.UpdateReviewResponse;
import com.travel.domain.review.entity.Review;
import com.travel.domain.review.repository.ReviewRepository;
import com.travel.global.exception.AccommodationException;
import com.travel.global.exception.MemberException;
import com.travel.global.exception.type.ErrorType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @InjectMocks
    ReviewService reviewService;

    @Mock
    ReviewRepository reviewRepository;

    @Mock
    AccommodationRepository accommodationRepository;

    @Mock
    MemberRepository memberRepository;

    @Mock
    ReservationRepository reservationRepository;

    @Mock
    ProductRepository productRepository;

    Long id = 1L;
    Long nonExistentId = 3L;
    LocalDate checkInDate = LocalDate.now().minusDays(3);
    LocalDate checkOutDate = checkInDate.plus(Period.ofDays(2));
    String checkInTime = "15:00";
    String checkOutTime = "11:00";


    @BeforeEach
    void setUp() {
    }

    @Test
    void getReviewList() {
        Member member = createMember();
        Accommodation accommodation = new Accommodation();
        AccommodationOption accommodationOption = createAccommodationOption(accommodation);
        AccommodationImage accommodationImage = createAccommodationImage(accommodation);
        Product product = new Product();
        List<Product> productList = new ArrayList<>();
        accommodation = createAccommodation(accommodationOption, accommodationImage, productList);
        ProductOption productOption = createProductOption(product, accommodation);
        ProductImage productImage = createProductImage(product, accommodation);
        List<ProductInfoPerNight> productInfoPerNightList = new ArrayList<>();
        product = createProduct(accommodation, productImage, productInfoPerNightList,
                productOption);
        productList.add(product);
        ProductInfoPerNight productInfoPerNight = createProductInfoPerNight(product, accommodation,
                checkInDate);
        ProductInfoPerNight productInfoPerNight2 = createProductInfoPerNight(product, accommodation,
                checkOutDate);
        productInfoPerNightList.add(productInfoPerNight);
        productInfoPerNightList.add(productInfoPerNight2);
        Review review = new Review();
        Reservation reservation = createReservation(member, accommodation, product);
        review = createReviewEntity(accommodation, member, reservation);

        accommodation.setReviews(List.of(review));
        accommodation.setProducts(List.of(product));

        when(accommodationRepository
                .findAccommodationById(anyLong())).thenReturn(accommodation);

        AccommodationReviewResponseList response = reviewService.getReviewList(id);
        assertNotNull(response);
        assertEquals(id, accommodation.getId());
    }

    @Test
    void getReviewList_NOT_FOUND() {
        when(accommodationRepository.findAccommodationById(nonExistentId))
                .thenThrow(new AccommodationException(ErrorType.NOT_FOUND));

        AccommodationException exception = assertThrows(AccommodationException.class, () -> {
            reviewService.getReviewList(nonExistentId);
        });
        assertEquals(ErrorType.NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    void getMyReviewList() {
        Member member = new Member();
        Accommodation accommodation = new Accommodation();
        AccommodationOption accommodationOption = createAccommodationOption(accommodation);
        AccommodationImage accommodationImage = createAccommodationImage(accommodation);
        Product product = new Product();
        List<Product> productList = new ArrayList<>();
        accommodation = createAccommodation(accommodationOption, accommodationImage, productList);
        ProductOption productOption = createProductOption(product, accommodation);
        ProductImage productImage = createProductImage(product, accommodation);
        List<ProductInfoPerNight> productInfoPerNightList = new ArrayList<>();
        product = createProduct(accommodation, productImage, productInfoPerNightList,
                productOption);
        productList.add(product);
        ProductInfoPerNight productInfoPerNight = createProductInfoPerNight(product, accommodation,
                checkInDate);
        ProductInfoPerNight productInfoPerNight2 = createProductInfoPerNight(product, accommodation,
                checkOutDate);
        productInfoPerNightList.add(productInfoPerNight);
        productInfoPerNightList.add(productInfoPerNight2);
        Review review = new Review();
        Reservation reservation = createReservation(member, accommodation, product);
        review = createReviewEntity(accommodation, member, reservation);
        List<Review> reviewList = List.of(review);
        member = authMember(reviewList);

        when(memberRepository.getMember(id)).thenReturn(member);
        List<ReviewResponse> response = reviewService.getMyReviewList(id);

        assertNotNull(response);
        assertEquals(1L, member.getId());

    }

    @Test
    void getMyReviewList_NOT_FOUND() {
        when(memberRepository.getMember(nonExistentId))
                .thenThrow(new MemberException(ErrorType.NOT_FOUND));

        MemberException exception = assertThrows(MemberException.class, () -> {
            reviewService.getMyReviewList(nonExistentId);
        });
        assertEquals(ErrorType.NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    void createReview() {
        Member member = createMember();
        Accommodation accommodation = new Accommodation();
        AccommodationOption accommodationOption = createAccommodationOption(accommodation);
        AccommodationImage accommodationImage = createAccommodationImage(accommodation);
        Product product = new Product();
        List<Product> productList = new ArrayList<>();
        accommodation = createAccommodation(accommodationOption, accommodationImage, productList);
        ProductOption productOption = createProductOption(product, accommodation);
        ProductImage productImage = createProductImage(product, accommodation);
        List<ProductInfoPerNight> productInfoPerNightList = new ArrayList<>();
        product = createProduct(accommodation, productImage, productInfoPerNightList,
                productOption);
        productList.add(product);
        ProductInfoPerNight productInfoPerNight = createProductInfoPerNight(product, accommodation,
                checkInDate);
        ProductInfoPerNight productInfoPerNight2 = createProductInfoPerNight(product, accommodation,
                checkOutDate);
        productInfoPerNightList.add(productInfoPerNight);
        productInfoPerNightList.add(productInfoPerNight2);
        Reservation reservation = createReservation(member, accommodation, product);
        Review review = createReviewEntity(accommodation, member, reservation);
        ReviewRequest reviewRequest = createReviewRequest();
        ReviewResponse reviewResponse = createReviewResponse(review);

        when(accommodationRepository.findAccommodationById(anyLong())).thenReturn(accommodation);
        when(reservationRepository.getReservationById(anyLong())).thenReturn(reservation);
        when(memberRepository.getMember(anyLong())).thenReturn(member);
        when(productRepository.findByIdAndAccommodationId(anyLong(), anyLong())).thenReturn(
                Optional.of(product));
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        // Then
        reviewResponse = reviewService.createReview(1L, 1L, reviewRequest);

        assertNotNull(reviewResponse);
        assertEquals(id, member.getId());
    }

    @Test
    void deleteReview() {
        Member member = createMember();
        Accommodation accommodation = new Accommodation();
        Review review = new Review();

        lenient().when(
                        reviewRepository.findByIdAndAccommodationId(review.getId(), accommodation.getId()))
                .thenReturn(Optional.of(review));

        DeleteReviewResponse reviewResponse
                = reviewService.deleteReview(member.getId(), accommodation.getId(), review.getId());

        assertNotNull(reviewResponse);
    }

    @Test
    void updateGrade() {
        Accommodation accommodation = new Accommodation();
        Member member = createMember();
        AccommodationOption accommodationOption = createAccommodationOption(accommodation);
        AccommodationImage accommodationImage = createAccommodationImage(accommodation);
        Product product = new Product();
        List<Product> productList = new ArrayList<>();
        ProductOption productOption = createProductOption(product, accommodation);
        ProductImage productImage = createProductImage(product, accommodation);
        List<ProductInfoPerNight> productInfoPerNightList = new ArrayList<>();
        product = createProduct(accommodation, productImage, productInfoPerNightList,
                productOption);
        productList.add(product);
        ProductInfoPerNight productInfoPerNight = createProductInfoPerNight(product, accommodation,
                checkInDate);
        ProductInfoPerNight productInfoPerNight2 = createProductInfoPerNight(product, accommodation,
                checkOutDate);
        productInfoPerNightList.add(productInfoPerNight);
        productInfoPerNightList.add(productInfoPerNight2);
        Review review = new Review();
        Reservation reservation = createReservation(member, accommodation, product);
        review = createReviewEntity(accommodation, member, reservation);
        List<Review> reviewList = List.of(review);
        accommodation = upgrageReviewAccommodation(accommodationOption, accommodationImage,
                productList, reviewList);

        when(accommodationRepository.findAll()).thenReturn(List.of(accommodation));
        when(reviewRepository.getByAccommodationId(anyLong())).thenAnswer(invocation -> {
            Long accommodationId = invocation.getArgument(0);
            if (accommodationId.equals(id)) {
                return reviewList;
            }
            return new ArrayList<>();
        });
        reviewService.updateGrade();
    }

    private UpdateReviewResponse createUpdateReviewResponse() {
        return UpdateReviewResponse.builder()
                .message("message")
                .comment("comment")
                .title("title")
                .grade(BigDecimal.valueOf(4.4))
                .updatedAt(LocalDateTime.now().plusHours(5))
                .build();
    }

    private Review createReviewEntity(Accommodation accommodation, Member member,
            Reservation reservation) {
        return Review.builder()
                .id(id)
                .title("title")
                .comment("comment")
                .grade(BigDecimal.valueOf(4.4))
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .accommodation(accommodation)
                .member(member)
                .reservation(reservation)
                .build();
    }

    private Reservation createReservation(Member member, Accommodation accommodation,
            Product product) {
        int night = (int) ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        int price = 300000;

        return Reservation.builder()
                .id(1L)
                .member(member)
                .accommodation(accommodation)
                .product(product)
                .price(price)
                .night(night)
                .personNumber(2)
                .checkInDate(checkInDate)
                .checkOutDate(checkOutDate)
                .build();
    }

    private Member createMember() {
        return Member.builder()
                .id(id)
                .email("test@gmail.com")
                .name("testMember")
                .password("testMember1@#$")
                .build();
    }

    private Member authMember(List<Review> review) {
        return Member.builder()
                .id(id)
                .email("test@gmail.com")
                .name("testMember")
                .password("testMember1@#$")
                .reviews(review)
                .build();
    }

    private ReviewRequest createReviewRequest() {
        return ReviewRequest.builder()
                .title("title")
                .comment("comment")
                .grade(BigDecimal.valueOf(4.5))
                .reservationId(id)
                .build();
    }

    private ReviewResponse createReviewResponse(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .accommodationName(review.getAccommodation().getName())
                .productName(review.getAccommodation().getProducts().get(0).getName())
                .memberName(review.getMember().getName())
                .reservationId(review.getReservation().getId())
                .title(review.getTitle())
                .comment(review.getComment())
                .grade(review.getGrade())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
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

    private Accommodation upgrageReviewAccommodation(
            AccommodationOption accommodationOption, AccommodationImage accommodationImage,
            List<Product> productList, List<Review> reviews) {
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
                .reviews(reviews)
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