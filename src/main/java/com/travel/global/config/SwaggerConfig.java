package com.travel.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.global.annotation.TokenMemberId;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

@Configuration
public class SwaggerConfig {

    @Bean
    public ModelResolver modelResolver(ObjectMapper objectMapper) {
        return new ModelResolver(objectMapper);
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .components(new Components())
            .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
            .title("[불사조] 숙박 예약 시스템 REST API")
            .version("1.0.0")
            .description("숙박 예약 시스템의 API 명세입니다.");
    }

    @Bean
    public OperationCustomizer customizeOperation() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            if (handlerMethod.getMethod().getParameters() != null) {
                for (java.lang.reflect.Parameter parameter : handlerMethod.getMethod().getParameters()) {
                    if (parameter.isAnnotationPresent(TokenMemberId.class)) {
                        operation.getParameters().removeIf(p -> p.getName().equals(parameter.getName()));
                    }
                }
            }
            return operation;
        };
    }
}
