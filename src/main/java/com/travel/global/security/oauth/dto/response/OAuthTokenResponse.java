package com.travel.global.security.oauth.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OAuthTokenResponse {

    private String token_type;
    private String access_token;
    private String refresh_token;
    private String id_token;
    private Integer expires_in;
    private Integer refresh_expires_in;
}
