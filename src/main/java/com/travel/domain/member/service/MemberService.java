package com.travel.domain.member.service;

import static com.travel.domain.member.type.SocialType.COMMON;
import static com.travel.global.exception.type.ErrorType.DUPLICATED_MEMBER;
import static com.travel.global.exception.type.ErrorType.INVALID_EMAIL_AND_PASSWORD;
import static com.travel.global.exception.type.ErrorType.INVALID_TOKEN;
import static com.travel.global.security.type.TokenType.ACCESS;
import static com.travel.global.security.type.TokenType.REFRESH;

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
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    private final TokenService tokenService;

    public MemberResponse signup(SignupRequest request) {
        String email = request.getEmail();

        if (memberRepository.existsByEmail(email)) {
            throw new MemberException(DUPLICATED_MEMBER);
        }

        String password = passwordEncoder.encode(request.getPassword());
        Member newMember = Member.from(email, request.getName(), password, COMMON.getType());

        Member savedMember = memberRepository.save(newMember);
        return MemberResponse.from(savedMember);
    }

    public LoginResponse login(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
        LoginRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new MemberException(INVALID_EMAIL_AND_PASSWORD));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new MemberException(INVALID_EMAIL_AND_PASSWORD);
        }

        String refreshToken = validateRefreshToken(httpRequest, member);

        String accessToken = jwtUtil.generateAccessToken(member.getId());

        addHeader(httpResponse, accessToken, refreshToken);

        tokenService.saveRefreshToken(member.getEmail(), refreshToken);
        return new LoginResponse(accessToken);
    }

    public Member oauthSignup(SignupRequest request, String type) {
        String email = request.getEmail();
        if (memberRepository.existsByEmail(email)) {
            throw new MemberException(DUPLICATED_MEMBER);
        }

        String password = passwordEncoder.encode(request.getPassword());
        Member newMember = Member.from(email, request.getName(), password, type);
        return memberRepository.save(newMember);
    }

    public LoginResponse oauthLogin(HttpServletRequest httpRequest,
        HttpServletResponse httpResponse,
        LoginRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new MemberException(INVALID_EMAIL_AND_PASSWORD));

        String refreshToken = validateRefreshToken(httpRequest, member);
        String accessToken = jwtUtil.generateAccessToken(member.getId());

        addHeader(httpResponse, accessToken, refreshToken);

        tokenService.saveRefreshToken(member.getEmail(), refreshToken);

        log.info("refresh token: {}", refreshToken);
        return new LoginResponse(accessToken);
    }

    private String validateRefreshToken(HttpServletRequest httpRequest, Member member) {
        String headerRefreshToken = httpRequest.getHeader(REFRESH.getName());
        String refreshToken = tokenService.getRefreshToken(member.getEmail());
        if (refreshToken == null) {
            refreshToken = jwtUtil.generateRefreshToken(member.getId());
        }

        if (headerRefreshToken != null && !headerRefreshToken.equals(refreshToken)) {
            throw new MemberException(INVALID_TOKEN);
        }

        return refreshToken;
    }

    @Transactional
    public void deleteMember(Long tokenMemberId) {
        Member member = memberRepository.findById(tokenMemberId)
            .orElseThrow(() -> new MemberException(INVALID_EMAIL_AND_PASSWORD));

        tokenService.removeToken(member.getEmail());
        memberRepository.delete(member);
    }

    @Transactional
    public void permanentlyDeleteOldMembers() {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        List<Member> membersDelete = memberRepository.findByDeletedAtIsNotNullAndDeletedAtBefore(
            oneMonthAgo);

        for (Member member : membersDelete) {
            memberRepository.deleteByEmail(member.getEmail());
        }
    }

    private void addHeader(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setHeader(ACCESS.getName(), accessToken);
        response.setHeader(REFRESH.getName(), refreshToken);
    }
}

