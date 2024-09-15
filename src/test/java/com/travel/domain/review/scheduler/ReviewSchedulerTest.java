package com.travel.domain.review.scheduler;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.travel.domain.review.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
class ReviewSchedulerTest {

    @Mock
    private ReviewService reviewService;

    private ReviewScheduler reviewScheduler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reviewScheduler = new ReviewScheduler(reviewService);
    }

    @Test
    void updateGrade() {
        doNothing().when(reviewService).updateGrade(); // reviewService.updateGrade()가 호출되면 아무 작업도 하지 않음

        // When
        reviewScheduler.updateGrade();

        // Then
       verify(reviewService, times(1)).updateGrade();
    }
}