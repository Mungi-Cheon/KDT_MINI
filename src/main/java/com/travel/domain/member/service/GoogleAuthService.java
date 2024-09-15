package com.travel.domain.member.service;

import static com.travel.domain.member.type.SocialType.GOOGLE;

import com.travel.domain.member.dto.request.LoginRequest;
import com.travel.domain.member.dto.request.SignupRequest;
import com.travel.domain.member.dto.response.LoginResponse;
import com.travel.domain.member.entity.Member;
import com.travel.domain.member.repository.MemberRepository;
import com.travel.global.security.oauth.client.GoogleClient;
import com.travel.global.security.oauth.dto.response.OAuthTokenResponse;
import com.travel.global.security.oauth.entity.OAuth2CustomMember;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoogleAuthService {

    private final GoogleClient googleClient;
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    public String getAuthUri() {
        return "https://accounts.google.com/o/oauth2/v2/auth"
            + "?client_id=" + clientId
            + "&redirect_uri=" + redirectUri
            + "&response_type=code"
            + "&scope=email%20profile"
            + "&access_type=offline"
            + "&prompt=consent";
    }

    public LoginResponse callBack(HttpServletRequest request, HttpServletResponse response,
        String code) {
        Member member = authenticateGoogle(code);

        LoginRequest loginRequest = LoginRequest.builder()
            .email(member.getEmail())
            .build();
        LoginResponse loginResponse = memberService.oauthLogin(request, response, loginRequest);
        setSecurityContext(member.getId());
        return loginResponse;
    }

    public Member authenticateGoogle(String code) {
        OAuthTokenResponse tokenResponse = googleClient.getGoogleToken(code);
        OAuth2CustomMember userInfo = googleClient.getGoogleUserInfo(
            tokenResponse.getAccess_token());

        Map<String, Object> attributes = userInfo.getAttributes();

        String name = (String) attributes.get("name");
        String email = (String) attributes.get("email");

        return memberRepository.findByEmail(email).orElseGet(
            () -> {
                SignupRequest signupRequest = SignupRequest.builder()
                    .name(name)
                    .email(email)
                    .password(userInfo.getSocialType() + (int) (Math.random() * 100000000))
                    .build();
                return memberService.oauthSignup(signupRequest, GOOGLE.getType());
            }
        );
    }

    private void setSecurityContext(Long memberId) {
        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(memberId, null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("Security context set complete. : {}", authentication);
    }
}
