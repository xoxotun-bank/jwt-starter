package com.example.jwtstarter.configuration;

import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.*;

@Slf4j
@Configuration
@ConditionalOnMissingBean({CorsConfig.class})
public class CorsConfig {

    @Value("${cors.allowed-origins}")
    private String[] allowedOrigins;

    @Value("${cors.allowed-headers}")
    private String[] allowedHeaders;

    @Value("${cors.allowed-methods}")
    private String[] allowedMethods;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                    .allowedOrigins(allowedOrigins)
                    .allowedHeaders(allowedHeaders)
                    .allowedMethods(allowedMethods);
                log.debug(
                    "CORS allowed origins: {}, headers: {}, methods: {}",
                    allowedOrigins,
                    allowedHeaders,
                    allowedMethods);
            }
        };
    }

}
