package com.travel.domain.review.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.travel.domain.accommodation.entity.Accommodation;
import com.travel.domain.reservations.entity.Reservation;
import com.travel.domain.review.entity.Review;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;


@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class ReviewRepositoryTest {

    Long id = 1L;

    @Mock
    private ReviewRepository reviewRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void findByIdAndAccommodationId() {
        Accommodation accommodation = Accommodation.builder()
                .id(id)
                .build();
        Review review = Review.builder()
                .id(id)
                .accommodation(accommodation)
                .build();

        when(reviewRepository
                .findByIdAndAccommodationId(id, id)).thenReturn(Optional.of(review));

        Optional<Review> result = reviewRepository.findByIdAndAccommodationId(id, id);

        assertNotNull(result);
        assertEquals(id, result.get().getAccommodation().getId());
        assertEquals(id, result.get().getId());
    }

    @Test
    void existsByReservationId(){

        //given
        Reservation reservation = Reservation.builder()
                .id(id)
                .build();

        when(reviewRepository.existsByReservationId(id)).thenReturn(true);

        //when
        boolean exists = reviewRepository.existsByReservationId(id);

        //then
        assertTrue(exists);
    }

    @Test
    void getByAccommodationId(){
        Accommodation accommodation = Accommodation.builder()
                .id(id)
                .build();
        Review review = Review.builder()
                .id(id)
                .accommodation(accommodation)
                .build();

        when(reviewRepository.getByAccommodationId(id)).thenReturn(List.of(review));

        List<Review> result = reviewRepository.getByAccommodationId(id);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(id, result.get(0).getId());
    }

    @Test
    void getByIdAndAccommodationId(){
        Accommodation accommodation = Accommodation.builder()
                .id(id)
                .build();
        Review review = Review.builder()
                .id(id)
                .accommodation(accommodation)
                .build();

        when(reviewRepository.getByIdAndAccommodationId(id, id)).thenReturn(review);

        Review result = reviewRepository.getByIdAndAccommodationId(id, id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(id, result.getAccommodation().getId());
    }
}