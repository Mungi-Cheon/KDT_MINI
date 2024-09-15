package com.travel.domain.like.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class LikeResponse {
    private Boolean liked;
    private int likeCount;
}
