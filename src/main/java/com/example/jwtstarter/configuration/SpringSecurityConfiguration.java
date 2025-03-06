package com.example.jwtstarter.configuration;

import com.example.jwtstarter.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.http.SessionCreationPolicy;

import com.example.jwtstarter.handler.CustomAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfiguration {

    private final JwtFilter jwtFilter;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults()) // Включение поддержки CORS
                .csrf(AbstractHttpConfigurer::disable) // Отключение CSRF, так как мы используем JWT
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) // Добавление фильтра JWT
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(authenticationEntryPoint) // Настройка обработки ошибок аутентификации
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/**").permitAll() // Открытые пути
                        .anyRequest().authenticated() // Требуется аутентификация для всех остальных путей
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Используем stateless сессии для JWT
                );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // Пример UserDetailsService для демонстрации
        return username -> User.builder()
                .username(username)
                .password(passwordEncoder().encode("password")) // Заглушка пароля
                .roles("USER")
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Бин для кодирования паролей
    }
}
