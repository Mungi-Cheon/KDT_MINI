package com.travel.domain.member.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.domain.member.dto.request.LoginRequest;
import com.travel.domain.member.dto.request.SignupRequest;
import com.travel.domain.member.dto.response.LoginResponse;
import com.travel.domain.member.dto.response.MemberResponse;
import com.travel.domain.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@TestPropertySource(properties = {
    "jwt.secret-key=your-secret-key-here",
    "jwt.issuer=your-issuer-here",
    "jwt.access-token-expire-time=600000",
    "jwt.refresh-token-expire-time=1200000"
})
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @Autowired
    private ObjectMapper objectMapper;

    private SignupRequest signupRequest;
    private LoginRequest loginRequest;
    private MemberResponse memberResponse;
    private LoginResponse loginResponse;

    @BeforeEach
    void setUp() {
        signupRequest = SignupRequest.builder()
            .email("test@example.com")
            .name("nickname")
            .password("password123")
            .build();

        loginRequest = LoginRequest.builder()
            .email("test@example.com")
            .password("password123")
            .build();

        memberResponse = MemberResponse.builder()
            .email("test@example.com")
            .name("nickname")
            .build();

        loginResponse = new LoginResponse("access-token-value");
    }

    @Test
    @DisplayName("회원가입 성공 테스트")
    void signup_success() throws Exception {
        given(memberService.signup(any(SignupRequest.class))).willReturn(memberResponse);

        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.email").value(memberResponse.email()))
            .andExpect(jsonPath("$.name").value(memberResponse.name()))
            .andDo(print());
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    void login_success() throws Exception {
        given(memberService.login(any(HttpServletRequest.class), any(HttpServletResponse.class),
            any(LoginRequest.class))).willReturn(loginResponse);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.access_token").value(loginResponse.accessToken()))
            .andDo(print());
    }

    @Test
    @DisplayName("회원가입 유효성 실패 테스트")
    void signup_validation_failure() throws Exception {
        SignupRequest invalidSignupRequest = SignupRequest.builder()
            .email("invalid-email")
            .name("nickname")
            .password("password123")
            .build();

        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidSignupRequest)))
            .andExpect(status().isBadRequest())
            .andDo(print());
    }

    @Test
    @DisplayName("로그인 유효성 실패 테스트")
    void login_validation_failure() throws Exception {
        LoginRequest invalidLoginRequest = LoginRequest.builder()
            .email("test@example.com")
            .password("short")
            .build();

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidLoginRequest)))
            .andExpect(status().isBadRequest())
            .andDo(print());
    }
}
