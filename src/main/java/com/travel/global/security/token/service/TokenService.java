package com.travel.global.security.token.service;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void saveRefreshToken(String username, String refreshToken) {
        redisTemplate.opsForValue().set(username, refreshToken, Duration.ofDays(7));
    }

    public String getRefreshToken(String username) {
        return (String) redisTemplate.opsForValue().get(username);
    }

    public void removeToken(String key) {
        redisTemplate.delete(key);
    }
}
