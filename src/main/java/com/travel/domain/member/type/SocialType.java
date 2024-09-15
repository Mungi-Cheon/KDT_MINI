package com.travel.domain.member.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SocialType {

    GOOGLE("GOOGLE"),
    NAVER("NAVER"),
    COMMON("COMMON");
    private final String type;

}
