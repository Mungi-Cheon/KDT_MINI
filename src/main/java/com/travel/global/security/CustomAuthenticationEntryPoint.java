package com.travel.global.security;

import static com.travel.global.exception.type.ErrorType.TOKEN_AUTHORIZATION_FAIL;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.global.config.ObjectMapperConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authException) throws IOException {

        ObjectMapper objectMapper = new ObjectMapperConfig().objectMapper();
        var responseResult = ResponseEntity.status(
                TOKEN_AUTHORIZATION_FAIL.getStatusCode())
            .body(TOKEN_AUTHORIZATION_FAIL.getMessage());
        String responseBody = objectMapper.writeValueAsString(responseResult);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(TOKEN_AUTHORIZATION_FAIL.getStatusCode().value());
        response.getWriter().write(responseBody);
        response.getWriter().flush();
    }
}