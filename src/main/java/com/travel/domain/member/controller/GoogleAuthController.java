package com.travel.domain.member.controller;

import com.travel.domain.member.dto.response.LoginResponse;
import com.travel.domain.member.dto.response.MemberResponse;
import com.travel.domain.member.service.GoogleAuthService;
import com.travel.global.exception.AuthException;
import com.travel.global.exception.type.ErrorType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Google Member API", description = "구글 회원 API")
@Slf4j
@RestController
@RequestMapping("/api/auth/google")
@RequiredArgsConstructor
public class GoogleAuthController {

    private final GoogleAuthService googleAuthService;

    @ApiResponse(responseCode = "200", description = "이 API는 사용하지 않고 아래 URL을 통해 소셜로그인 진행해주세요. <br>"
        + "https://accounts.google.com/o/oauth2/v2/auth?client_id=440174768732-dij059hiubr1upc9j2mhm7fa7ue1kqgf.apps.googleusercontent.com&redirect_uri=http://localhost:8080/api/auth/google/callback&response_type=code&scope=email%20profile&access_type=offline&prompt=consent",
        content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = MemberResponse.class)))
    @GetMapping("/login")
    public void login(HttpServletResponse response)
        throws IOException {
        String googleAuthUri = googleAuthService.getAuthUri();
        log.info("login url: {}", googleAuthUri);

        response.sendRedirect(googleAuthUri);
    }

    @Operation(summary = "소셜 로그인 콜백", description = "인증/인가 결과를 토대로 회원가입 및 로그인을 합니다.")
    @ApiResponse(responseCode = "200", description = "로그인 성공",
        content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = MemberResponse.class)))
    @GetMapping("/callback")
    public ResponseEntity<LoginResponse> callback(HttpServletRequest request,
        HttpServletResponse response,
        @RequestParam(value = "code", required = false) String code,
        @RequestParam(value = "error", required = false) String error) throws IOException {
        if (error != null) {
            throw new AuthException(ErrorType.TOKEN_AUTHORIZATION_FAIL);
        }
        LoginResponse loginResponse = googleAuthService.callBack(request, response, code);
        return ResponseEntity.ok(loginResponse);
    }
}
