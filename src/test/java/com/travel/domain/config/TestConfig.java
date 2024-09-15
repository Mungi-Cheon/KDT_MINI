package com.travel.domain.config;

import com.travel.domain.member.service.GoogleAuthService;
import com.travel.global.security.oauth.client.GoogleClient;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean
    public GoogleAuthService googleAuthService() {
        return Mockito.mock(GoogleAuthService.class);
    }

    @Bean
    public GoogleClient googleClient() {
        return Mockito.mock(GoogleClient.class);
    }
}
