
        package com.example.knowledge_management.config;

import org.springdoc.core.customizers.ServerBaseUrlCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;

@Configuration
public class ServerBaseUrlConfig {

    @Bean
    public ServerBaseUrlCustomizer serverBaseUrlCustomizer() {
        return new ServerBaseUrlCustomizer() {

            @Override
            public String customize(String serverBaseUrl, HttpRequest request) {
                return "https://knowledge-management-production-db46.up.railway.app";
            }
        };
    }
}

