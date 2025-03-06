package com.example.jwtstarter;

import org.springframework.boot.context.properties.*;

@ConfigurationProperties(prefix = "jwt-starter-properties")
public record JwtProperties(
    String publicKey,
    String algorithmFamily,
    String algorithmType,
    String zoneId,
    Integer errorCodeResponse
) {

}
