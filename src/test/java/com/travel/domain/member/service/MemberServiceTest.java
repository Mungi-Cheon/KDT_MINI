package com.travel.domain.member.service;

import static com.travel.global.exception.type.ErrorType.DUPLICATED_MEMBER;
import static com.travel.global.exception.type.ErrorType.INVALID_EMAIL_AND_PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.travel.domain.member.dto.request.LoginRequest;
import com.travel.domain.member.dto.request.SignupRequest;
import com.travel.domain.member.dto.response.LoginResponse;
import com.travel.domain.member.dto.response.MemberResponse;
import com.travel.domain.member.entity.Member;
import com.travel.domain.member.repository.MemberRepository;
import com.travel.global.exception.MemberException;
import com.travel.global.security.token.service.TokenService;
import com.travel.global.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private TokenService tokenService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    private SignupRequest signupRequest;
    private LoginRequest loginRequest;
    private HttpServletRequest httpRequest;
    private HttpServletResponse httpResponse;
    private Member member;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        signupRequest = new SignupRequest("test@example.com", "password123", "nickname");
        loginRequest = new LoginRequest("test@example.com", "password123");
        httpRequest = mock(HttpServletRequest.class);
        httpResponse = mock(HttpServletResponse.class);

        member = Member.builder()
            .email("test@example.com")
            .name("nickname")
            .password("encodedPassword123")
            .build();
    }

    @Test
    @DisplayName("회원 가입 성공 테스트")
    void signup_Success() {
        when(memberRepository.existsByEmail(signupRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(signupRequest.getPassword())).thenReturn("encodedPassword123");
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        MemberResponse response = memberService.signup(signupRequest);

        assertThat(response.email()).isEqualTo(member.getEmail());
        assertThat(response.name()).isEqualTo(member.getName());
    }

    @Test
    @DisplayName("중복 회원 가입 실패 테스트")
    void signup_Fail_DuplicatedMember() {
        when(memberRepository.existsByEmail(signupRequest.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> memberService.signup(signupRequest))
            .isInstanceOf(MemberException.class)
            .hasMessage(DUPLICATED_MEMBER.getMessage());
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    void login_Success() {
        when(memberRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(member));
        when(passwordEncoder.matches(loginRequest.getPassword(), member.getPassword()))
            .thenReturn(true);
        when(jwtUtil.generateAccessToken(member.getId())).thenReturn("accessToken");
        LoginResponse response = memberService.login(httpRequest, httpResponse, loginRequest);

        assertThat(response.accessToken()).isEqualTo("accessToken");
    }

    @Test
    @DisplayName("로그인 실패 테스트 - 회원 없음")
    void login_Fail_MemberNotFound() {
        when(memberRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(member));
        assertThatThrownBy(() -> memberService.login(httpRequest, httpResponse, loginRequest))
            .isInstanceOf(MemberException.class)
            .hasMessage(INVALID_EMAIL_AND_PASSWORD.getMessage());
    }

    @Test
    @DisplayName("로그인 실패 테스트 - 비밀번호 불일치")
    void login_Fail_IncorrectPassword() {
        when(memberRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(member));
        when(passwordEncoder.matches(loginRequest.getPassword(), member.getPassword())).thenReturn(
            false);

        assertThatThrownBy(() -> memberService.login(httpRequest, httpResponse, loginRequest))
            .isInstanceOf(MemberException.class)
            .hasMessage(INVALID_EMAIL_AND_PASSWORD.getMessage());
    }
}
