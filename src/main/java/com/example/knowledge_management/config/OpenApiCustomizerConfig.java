
        package com.example.knowledge_management.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiCustomizerConfig {

    @Bean
    public OpenApiCustomizer openApiCustomizer() {
        return openAPI -> openAPI.setServers(
                List.of(
                        new Server()
                                .url("https://knowledge-management-production-db46.up.railway.app")
                                .description("Railway Production Server")
                )
        );
    }
}


