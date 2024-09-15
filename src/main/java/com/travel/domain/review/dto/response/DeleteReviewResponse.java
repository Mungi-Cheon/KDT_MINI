package com.travel.domain.review.dto.response;


import com.travel.domain.review.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeleteReviewResponse {

    private String message;

    public static DeleteReviewResponse from (Review review) {
        return DeleteReviewResponse.builder()
            .message("해당 리뷰가 삭제 되었습니다.")
            .build();
    }
}
