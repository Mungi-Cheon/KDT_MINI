package com.travel.global.config;

import com.travel.global.resolver.TokenMemberIdResolver;
import com.travel.global.security.CustomAuthenticationEntryPoint;
import com.travel.global.security.filter.JwtFilter;
import com.travel.global.util.JwtUtil;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig implements WebMvcConfigurer {

    private final JwtUtil jwtUtil;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final TokenMemberIdResolver tokenMemberIdResolver;

    private final List<String> PASS_URL = List.of(
        "/api/auth/login", "/api/auth/signup",
        "/api/accommodations/**", "/api/review/**",
        "/", "/swagger-ui/**", "/swagger-ui/index.html",
        "/v3/api-docs/**", "favicon.ico", "/error",
        "/api/auth/google/**"
    );


    @Bean
    public static PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
        throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .headers(headers ->
                headers.frameOptions(FrameOptionsConfig::disable))
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(
                config -> config.authenticationEntryPoint(customAuthenticationEntryPoint))
            .authorizeHttpRequests(auth ->
                auth.requestMatchers("/api/auth/login", "/api/auth/signup",
                        "/api/accommodations/**", "/api/review/**", "/api/auth/google/**").permitAll()
                    .requestMatchers("/", "/swagger-ui/**", "/swagger-ui/index.html",
                        "/v3/api-docs/**", "favicon.ico", "/error").permitAll()
                    .anyRequest().authenticated())
            .addFilterBefore(
                new JwtFilter(jwtUtil, PASS_URL),
                UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(tokenMemberIdResolver);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
            .allowCredentials(false)
            .maxAge(1728000L)
            .allowedHeaders("*")
            .exposedHeaders("*");
    }
}
