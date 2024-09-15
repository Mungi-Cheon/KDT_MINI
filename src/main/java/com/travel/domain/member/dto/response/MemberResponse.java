package com.travel.domain.member.dto.response;

import com.travel.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record MemberResponse(

    @Schema(example = "fastfam@google.com")
    Long id, String email, String name) {

    public static MemberResponse from(Member member) {
        return new MemberResponse(
            member.getId(),
            member.getEmail(),
            member.getName());
    }
}
