package com.travel.domain.reservations.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.travel.domain.accommodation.entity.Accommodation;
import com.travel.domain.accommodation.entity.AccommodationImage;
import com.travel.domain.accommodation.entity.AccommodationOption;
import com.travel.domain.member.entity.Member;
import com.travel.domain.product.entity.Product;
import com.travel.domain.product.entity.ProductImage;
import com.travel.domain.product.entity.ProductInfoPerNight;
import com.travel.domain.product.entity.ProductOption;
import com.travel.domain.reservations.entity.Reservation;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ReservationRepositoryTest {

    @Mock
    private ReservationRepository reservationRepository;

    private Accommodation accommodation;

    private AccommodationOption accommodationOption;

    private AccommodationImage accommodationImage;
    private Product product;

    private ProductImage productImage;

    private ProductInfoPerNight productInfoPerNight;

    private ProductOption productOption;

    @BeforeEach
    public void setUp() {
        accommodation = new Accommodation();
        accommodationOption = createAccommodationOption(accommodation);
        accommodationImage = createAccommodationImage(accommodation);
        product = new Product();
        accommodation = createAccommodation();
        productOption = createProductOption();
        productImage = createProductImage();
        productInfoPerNight = createProductInfoPerNight();
        product = createProduct();
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName(value = "예약하기")
    void testSave() {
        Member member = createMember("test@gmail.com");

        LocalDate checkInDate = LocalDate.now();
        LocalDate checkOutDate = checkInDate.plusDays(1);
        int personNumber = 2;

        Reservation reservation = createReservation(member, checkInDate, checkOutDate,
            personNumber);
        when(reservationRepository.saveAll(any())).thenReturn(List.of(reservation));
        List<Reservation> savedList = reservationRepository.saveAll(List.of(reservation));

        assertNotNull(savedList);
        assertEquals(1, savedList.size());
        assertEquals(reservation.getId(), savedList.get(0).getId());
    }

    @Test
    @DisplayName(value = "예약 내역 조회")
    void testFindByMemberId() {
        Member member = createMember("test100@gmail.com");

        LocalDate checkInDate = LocalDate.now();
        LocalDate checkOutDate = checkInDate.plusDays(1);
        int personNumber = 2;

        Reservation reservation = createReservation(member, checkInDate, checkOutDate,
            personNumber);

        when(reservationRepository.saveAll(any())).thenReturn(List.of(reservation));
        List<Reservation> savedList = reservationRepository.saveAll(List.of(reservation));
        assertNotNull(savedList);

        when(reservationRepository.findByMemberId(anyLong())).thenReturn(savedList);
        List<Reservation> result = reservationRepository.findByMemberId(member.getId());

        assertNotNull(result);
        assertEquals(savedList.get(0).getId(), result.get(0).getId());
    }

    @Test
    @DisplayName(value = "동일한 예약한 날짜와 예약 상품이 존재하는 경우")
    void testFindAlreadyReservationWithPessimisticLock() {
        Member member = createMember("test101@gmail.com");

        LocalDate checkInDate = LocalDate.now();
        LocalDate checkOutDate = checkInDate.plusDays(2);
        int personNumber = 2;

        Reservation reservation = createReservation(member, checkInDate, checkOutDate,
            personNumber);
        when(reservationRepository.saveAll(any())).thenReturn(List.of(reservation));

        when(reservationRepository.findAlreadyReservationWithPessimisticLock(anyLong(), anyLong(),
            any(), any())).thenReturn(List.of(reservation));

        List<Reservation> savedList = reservationRepository.saveAll(List.of(reservation));

        assertNotNull(savedList);

        List<Reservation> result = reservationRepository
            .findAlreadyReservationWithPessimisticLock(member.getId(), product.getId(), checkInDate,
                checkOutDate);

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName(value = "예약 취소")
    void testReservationCancel() {
        Member member = createMember("test111@gmail.com");

        LocalDate checkInDate = LocalDate.now();
        LocalDate checkOutDate = checkInDate.plusDays(2);
        int personNumber = 2;

        Reservation reservation = createReservation(member, checkInDate, checkOutDate,
            personNumber);

        when(reservationRepository.saveAll(any())).thenReturn(List.of(reservation));
        List<Reservation> savedList = reservationRepository.saveAll(List.of(reservation));

        assertNotNull(savedList);

        reservationRepository.delete(reservation);

        Reservation result = reservationRepository
            .getReservationByIdAndMemberId(reservation.getId(), member.getId());

        assertNull(result);
    }

    private Member createMember(String email) {
        return Member.builder()
            .id(1L)
            .email(email)
            .name("testMember")
            .password("testMember1@#$")
            .build();
    }

    private Product createProduct() {
        return Product.builder()
            .id(30L)
            .accommodation(accommodation)
            .checkInTime("15:00")
            .checkOutTime("10:00")
            .description("description description")
            .type("Standard")
            .name("테스트 룸")
            .standardNumber(2)
            .maximumNumber(4)
            .productOption(productOption)
            .productInfoPerNightsList(List.of(productInfoPerNight))
            .productImage(productImage)
            .build();
    }

    private Accommodation createAccommodation() {
        return Accommodation.builder()
            .name("숙소")
            .category("호텔")
            .description("테스트 호텔입니다")
            .address("테스트도 테스트시 테스트구 123-1")
            .grade(BigDecimal.valueOf(5.0))
            .contact("0503-1111-1111")
            .options(accommodationOption)
            .images(accommodationImage)
            .products(List.of(product))
            .build();
    }

    private ProductImage createProductImage() {
        return ProductImage.builder()
            .accommodation(accommodation)
            .product(product)
            .imageUrl1("Image1")
            .imageUrl2("Image2")
            .build();
    }

    private ProductInfoPerNight createProductInfoPerNight() {
        return ProductInfoPerNight.builder()
            .accommodation(accommodation)
            .product(product)
            .count(1)
            .price(200000)
            .date(LocalDate.now())
            .build();
    }

    private Reservation createReservation(Member member,
        LocalDate checkInDate, LocalDate checkOutDate,
        int personNumber) {
        int night = (int) ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        int price = 300000;

        return Reservation.builder()
            .id(1L)
            .member(member)
            .accommodation(accommodation)
            .product(product)
            .price(price)
            .night(night)
            .personNumber(personNumber)
            .checkInDate(checkInDate)
            .checkOutDate(checkOutDate)
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

    private AccommodationImage createAccommodationImage(Accommodation accommodation) {
        return AccommodationImage.builder()
            .id(1L)
            .accommodation(accommodation)
            .thumbnail("thumbNailImage")
            .imageUrl1("imgUrl1")
            .imageUrl2("imgUrl2")
            .build();
    }

    private AccommodationOption createAccommodationOption(Accommodation accommodation) {
        return AccommodationOption.builder()
            .id(1L)
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
}