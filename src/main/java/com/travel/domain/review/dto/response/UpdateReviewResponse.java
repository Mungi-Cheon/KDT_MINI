package com.travel.domain.review.dto.response;


import com.travel.domain.review.entity.Review;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class UpdateReviewResponse {

    private String message;

    private String comment;

    private String title;

    private BigDecimal grade;

    private LocalDateTime updatedAt;

    public static UpdateReviewResponse from(Review review){
        return UpdateReviewResponse.builder()
            .message("수정이 완료되었습니다")
            .comment(review.getComment())
            .title(review.getTitle())
            .grade(review.getGrade())
            .grade(review.getGrade())
            .updatedAt(LocalDateTime.now())
            .build();
    }
}
