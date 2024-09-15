package com.travel.domain.review.dto.response;

import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class AccommodationReviewResponseList {

    private Long id;

    private String name;

    private String thumbnail;

    private BigDecimal grade;

    private List<ReviewResponse> reviewResponseList;

    public static AccommodationReviewResponseList from(String thumbnail, Long accommodationId,
            String accommodationName, BigDecimal grade,
            List<ReviewResponse> reviewResponses) {
        return AccommodationReviewResponseList.builder()
                .id(accommodationId)
                .name(accommodationName)
                .grade(grade)
                .thumbnail(thumbnail)
                .reviewResponseList(reviewResponses)
                .build();
    }
}
