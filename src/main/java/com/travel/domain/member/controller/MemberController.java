package com.travel.domain.member.controller;

import com.travel.domain.member.dto.request.LoginRequest;
import com.travel.domain.member.dto.request.SignupRequest;
import com.travel.domain.member.dto.response.LoginResponse;
import com.travel.domain.member.dto.response.MemberResponse;
import com.travel.domain.member.service.MemberService;
import com.travel.global.annotation.TokenMemberId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Member API", description = "회원 API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원 가입", description = "회원 정보를 등록합니다.")
    @ApiResponse(responseCode = "201", description = "회원 가입 성공",
        content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = MemberResponse.class)))
    @PostMapping("/signup")
    public ResponseEntity<MemberResponse> signup(
        @RequestBody @Valid SignupRequest signupRequest) {
        MemberResponse response = memberService.signup(signupRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "로그인 ", description = "이메일과 비밀번호로 로그인합니다.")
    @ApiResponse(description = "로그인 성공",
        content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = LoginResponse.class)))
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(HttpServletRequest request,
        HttpServletResponse response,
        @RequestBody @Valid LoginRequest loginRequest) {
        LoginResponse loginResponse = memberService.login(request, response, loginRequest);
        return ResponseEntity.ok().body(loginResponse);
    }

    @Operation(summary = "회원 탈퇴", description = "회원 계정을 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "회원 탈퇴 성공")
    @DeleteMapping("/mypage/unregister")
    public ResponseEntity<Void> unregister(@TokenMemberId Long tokenMemberId) {
        memberService.deleteMember(tokenMemberId);
        return ResponseEntity.ok().build();
    }
}
