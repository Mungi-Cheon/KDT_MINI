package com.travel;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@OpenAPIDefinition(
    servers = {
        @Server(url = "/", description = "Default Server url")
    }
)
@SpringBootApplication
@EnableAspectJAutoProxy
@EnableJpaAuditing
@EnableCaching
@EnableScheduling
public class KdtBe8MiniProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(KdtBe8MiniProjectApplication.class, args);
    }

}
