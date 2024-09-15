package com.travel.domain.review.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String comment;

    @NotNull
    private BigDecimal grade;

    @NotNull
    private Long reservationId;

}
