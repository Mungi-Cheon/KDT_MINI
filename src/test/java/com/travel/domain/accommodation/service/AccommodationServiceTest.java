package com.travel.domain.accommodation.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.travel.domain.accommodation.dto.response.AccommodationResponse;
import com.travel.domain.accommodation.entity.Accommodation;
import com.travel.domain.accommodation.entity.AccommodationImage;
import com.travel.domain.accommodation.repository.AccommodationRepository;
import com.travel.domain.product.entity.Product;
import com.travel.domain.product.entity.ProductInfoPerNight;
import com.travel.domain.product.repository.ProductInfoPerNightRepository;
import com.travel.domain.product.repository.ProductRepository;
import com.travel.global.exception.AccommodationException;
import com.travel.global.exception.type.ErrorType;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccommodationServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private AccommodationRepository accommodationRepository;

    @Mock
    private ProductInfoPerNightRepository productInfoPerNightRepository;

    @InjectMocks
    private AccommodationService accommodationService;

    private Accommodation accommodation;

    private Product product;

    private ProductInfoPerNight productInfoPerNight;

    private AccommodationImage accommodationImage;

    @BeforeEach
    public void setUp() {
        productInfoPerNight = ProductInfoPerNight.builder()
            .price(100000)
            .build();

        product = Product.builder()
            .type("Standard")
            .standardNumber(2)
            .maximumNumber(4)
            .productInfoPerNightsList(List.of(productInfoPerNight))
            .build();

        accommodationImage = AccommodationImage.builder().build();

        accommodation = Accommodation.builder()
            .products(List.of(product))
            .images(accommodationImage)
            .build();
    }

    @Test
    @DisplayName("성공 사례")
    public void testGetAvailableAccommodations_success() {
        // given
        String category = "호텔";
        LocalDate checkIn = LocalDate.now();
        LocalDate checkOut = checkIn.plusDays(1);
        int personNumber = 2;

        when(accommodationRepository.findAccommodationsByCategory(any(), any()))
            .thenReturn(List.of(accommodation));

        // when
        List<AccommodationResponse> result = accommodationService.getAvailableAccommodations(
            category, checkIn, checkOut, personNumber, null);

        // then
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(accommodation.getId(), result.get(0).getId());
        assertEquals(100000, result.get(0).getPrice());
    }

    @Test
    @DisplayName("실패 사례 : 조건에 해당하는 숙소가 존재하지 않음")
    public void testGetAvailableAccommodations_noAccommodationsFound() {
        // given
        String category = "호텔";
        LocalDate checkIn = LocalDate.now();
        LocalDate checkOut = checkIn.plusDays(1);
        int personNumber = 2;

        when(accommodationRepository.findAccommodationsByCategory(any(), any()))
            .thenReturn(List.of());

        // when
        AccommodationException exception = assertThrows(AccommodationException.class,
            () -> accommodationService
                .getAvailableAccommodations(category, checkIn, checkOut, personNumber, null));

        // then
        assertEquals(ErrorType.NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("실패 사례 : 체크인 날짜가 올바르지 않음")
    void getAvailableAccommodations_InvalidCheckIn_ThrowsException() {
        // given
        String category = "호텔";
        LocalDate checkIn = LocalDate.now().minusDays(1);
        LocalDate checkOut = checkIn.plusDays(2);
        int personNumber = 2;

        // when
        AccommodationException exception = assertThrows(AccommodationException.class,
            () -> accommodationService
                .getAvailableAccommodations(category, checkIn, checkOut, personNumber, null));

        // then
        assertEquals(ErrorType.INVALID_CHECK_IN.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("실패 사례 : 체크아웃 날짜가 올바르지 않음")
    void getAvailableAccommodations_InvalidCheckOut_ThrowsException() {
        // given
        String category = "호텔";
        LocalDate checkIn = LocalDate.now();
        LocalDate checkOut = checkIn.minusDays(2);
        int personNumber = 2;

        // when
        AccommodationException exception = assertThrows(AccommodationException.class,
            () -> accommodationService
                .getAvailableAccommodations(category, checkIn, checkOut, personNumber, null));

        // then
        assertEquals(ErrorType.INVALID_CHECK_OUT.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("실패 사례 : 인원이 올바르지 않음")
    void getAvailableAccommodations_InvalidPersonNumber_ThrowsException() {
        // given
        String category = "호텔";
        LocalDate checkIn = LocalDate.now();
        LocalDate checkOut = checkIn.plusDays(1);
        int personNumber = 0;

        // when
        AccommodationException exception = assertThrows(AccommodationException.class,
            () -> accommodationService
                .getAvailableAccommodations(category, checkIn, checkOut, personNumber, null));

        // then
        assertEquals(ErrorType.INVALID_NUMBER_OF_PEOPLE.getMessage(), exception.getMessage());
    }
}
