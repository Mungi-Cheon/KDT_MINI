package com.travel.global.resolver;

import static com.travel.global.security.type.TokenType.ACCESS;

import com.travel.global.annotation.TokenMemberId;
import com.travel.global.exception.MemberException;
import com.travel.global.exception.type.ErrorType;
import com.travel.global.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class TokenMemberIdResolver implements HandlerMethodArgumentResolver {

    private final JwtUtil jwtUtil;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 지원하는 파라미터 체크, 어노테이션 체크
        boolean annotation = parameter.hasParameterAnnotation(TokenMemberId.class);
        boolean parameterType = parameter.getParameterType().equals(Long.class);

        return (annotation && parameterType);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        Long memberId = extractMemberId(request);
        validateMemberId(memberId);

        return memberId;
    }

    private Long extractMemberId(HttpServletRequest request) {
        String accessToken = request.getHeader(ACCESS.getName());

        if (accessToken != null) {
            return jwtUtil.getAccessTokenMemberId(accessToken);
        } else {
            return (Long) request.getAttribute("memberId");
        }
    }

    private void validateMemberId(Long memberId) {
        if (memberId == null) {
            throw new MemberException(ErrorType.INVALID_EMAIL_AND_PASSWORD);
        }
    }
}
