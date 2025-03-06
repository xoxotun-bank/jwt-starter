package com.example.jwtstarter;

import java.io.*;
import java.util.*;
import java.util.stream.*;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.authority.*;
import org.springframework.security.core.context.*;
import org.springframework.web.filter.*;

import com.example.jwtstarter.exception.*;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends GenericFilterBean {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    public static final String AUTHORIZATION_REFIX = "Bearer ";

    private final JwtValidationService jwtValidationService;

    public static JwtFilter of(
        JwtValidationService jwtValidationService
    ) {
        return new JwtFilter(jwtValidationService);
    }

    @Override
    public void doFilter(
        ServletRequest servletRequest,
        ServletResponse servletResponse,
        FilterChain filterChain
    ) throws IOException, ServletException {

        var request = (HttpServletRequest) servletRequest;
        var response = (HttpServletResponse) servletResponse;

        var header = request.getHeader(AUTHORIZATION_HEADER);

        log.debug("Request URI: {}, method: {}", request.getRequestURI(), request.getMethod());

        if (header != null && header.startsWith(AUTHORIZATION_REFIX)) {

            try {
                var token = header.substring(AUTHORIZATION_REFIX.length());
                var jwtToken = jwtValidationService.extractAccessTokenPayload(token);

                List<SimpleGrantedAuthority> authorityList = jwtToken.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toList());

                var authenticationToken = new UsernamePasswordAuthenticationToken(
                    jwtToken.getUserId(),
                    null,
                    authorityList);
                authenticationToken.setDetails(token);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                filterChain.doFilter(request, response);
                return;
            } catch (InvalidJwtTokenException e) {
                log.error("Invalid JWT Token", e);
            }
        }
        try {
            filterChain.doFilter(request, response);
        } finally {
            SecurityContextHolder.clearContext();
        }
    }

}
