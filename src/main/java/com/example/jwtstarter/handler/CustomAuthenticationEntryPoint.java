package com.example.jwtstarter.handler;

import java.io.*;


import com.fasterxml.jackson.databind.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.http.*;
import org.springframework.security.core.*;
import org.springframework.security.web.*;

import com.example.jwtstarter.*;

@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final JwtProperties jwtProperties;

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void commence(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException authException
    ) throws IOException, ServletException {

        log.error(
            "Handling authentication exception ",
            authException
        );

        ApiErrorResponseDto apiErrorResponseDto = new ApiErrorResponseDto();
        apiErrorResponseDto.setMessage("AccessToken не валиден");
        apiErrorResponseDto.setCode(jwtProperties.errorCodeResponse());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        OutputStream responseStream = response.getOutputStream();
        responseStream.write(mapper.writeValueAsBytes(apiErrorResponseDto));

        responseStream.flush();
    }

}
