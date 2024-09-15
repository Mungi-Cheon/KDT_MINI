package com.travel.domain.review.scheduler;

import com.travel.domain.review.service.ReviewService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
//@EnableAsync 병렬적 사용
public class ReviewScheduler {

    private final ReviewService reviewService;

    // 매일 자정 실행
    //@Async 비동기
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateGrade() {
        try {
            reviewService.updateGrade();
            log.info("현재 시간 : {}", LocalDateTime.now());
        }catch (Exception e ) {
            log.error("Error updating grades: {}", e.getMessage(), e);
        }
    }
}
