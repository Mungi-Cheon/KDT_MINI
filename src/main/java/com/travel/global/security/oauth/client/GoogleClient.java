package com.travel.global.security.oauth.client;

import static com.travel.domain.member.type.SocialType.GOOGLE;

import com.travel.global.exception.AuthException;
import com.travel.global.exception.type.ErrorType;
import com.travel.global.security.oauth.dto.response.OAuthTokenResponse;
import com.travel.global.security.oauth.entity.OAuth2CustomMember;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class GoogleClient {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    private final String TOKEN_URI = "https://oauth2.googleapis.com/token";
    private final String USER_INFO_URI = "https://www.googleapis.com/oauth2/v3/userinfo";

    private final RestTemplate restTemplate = new RestTemplate();

    public OAuthTokenResponse getGoogleToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<OAuthTokenResponse> response = restTemplate.postForEntity(TOKEN_URI, request,
            OAuthTokenResponse.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new AuthException(ErrorType.TOKEN_AUTHORIZATION_FAIL);
        }
    }

    public OAuth2CustomMember getGoogleUserInfo(String googleAccessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + googleAccessToken);
        HttpEntity<String> entity = new HttpEntity<>("", headers);

        ResponseEntity<Map> response = restTemplate.exchange(
            USER_INFO_URI,
            HttpMethod.GET,
            entity,
            Map.class
        );
        return new OAuth2CustomMember(GOOGLE.getType(), response.getBody(), GOOGLE.getType(), null);
    }
}
