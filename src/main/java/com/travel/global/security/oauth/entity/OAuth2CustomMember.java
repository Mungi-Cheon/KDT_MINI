package com.travel.global.security.oauth.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Getter
@AllArgsConstructor
public class OAuth2CustomMember implements OAuth2User, Serializable {

    private String name;
    private Map<String, Object> attributes;
    private String socialType;
    private List<GrantedAuthority> authorities;
}
