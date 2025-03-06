package com.example.jwtstarter.configuration;

import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.context.properties.*;
import org.springframework.context.annotation.*;

import com.example.jwtstarter.*;
import com.example.jwtstarter.handler.*;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class JwtStarterAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public JwtValidationService jwtValidationService(JwtProperties jwtProperties) {
        return JwtValidationService.of(jwtProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtFilter jwtFilter(
        JwtValidationService jwtValidationService
    ) {
        return JwtFilter.of(jwtValidationService);
    }

    @Bean
    @ConditionalOnMissingBean
    public CustomAuthenticationEntryPoint customAuthenticationEntryPoint(JwtProperties jwtProperties) {
        return new CustomAuthenticationEntryPoint(jwtProperties);
    }

}
