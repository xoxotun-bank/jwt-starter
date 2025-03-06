package com.example.jwtstarter;

import java.security.*;
import java.security.interfaces.*;
import java.security.spec.*;
import java.time.*;
import java.util.*;

import com.auth0.jwt.*;
import com.auth0.jwt.exceptions.*;
import com.auth0.jwt.interfaces.*;
import lombok.*;
import lombok.extern.slf4j.*;

import com.example.jwtstarter.exception.*;
import com.example.jwtstarter.token.*;

@Slf4j
@RequiredArgsConstructor
public class JwtValidationService {

    private final RSAPublicKey publicKey;

    private final JwtProperties jwtProperties;

    private final ZoneId timeZone;

    public static JwtValidationService of(JwtProperties jwtProperties) {
        var key = buildPubKey(jwtProperties.publicKey(), jwtProperties.algorithmFamily());
        return new JwtValidationService(key, jwtProperties, ZoneId.of(jwtProperties.zoneId()));
    }

    static RSAPublicKey buildPubKey(String publicKey, String algorithm) {
        var keyBytes = Base64.getDecoder().decode(publicKey);
        var keySpec = new X509EncodedKeySpec(keyBytes);
        try {
            var keyFactory = KeyFactory.getInstance(algorithm);
            var key = (RSAPublicKey) keyFactory.generatePublic(keySpec);
            return key;
        } catch (Exception ex) {
            throw new RuntimeException(String.format(
                "Error on building public key(%s): %s",
                algorithm, publicKey
            ), ex);
        }
    }

    public JwtAccessToken extractAccessTokenPayload(String jwtValue) {
        var decodedJwt = getDecodedJwt(jwtValue);

        var token = JwtAccessToken.builder()
            .roles(decodedJwt.getClaim("roles").asList(String.class))
            .userId(decodedJwt.getClaim("user_id").asInt())
            .city(decodedJwt.getClaim("city").asString())
            .region(decodedJwt.getClaim("region").asString())
            .expiredAt(decodedJwt.getExpiresAtAsInstant().atZone(timeZone))
            .issuedAt(decodedJwt.getIssuedAtAsInstant().atZone(timeZone))
            .jti(UUID.fromString(decodedJwt.getId()))
            .build();

        return token;
    }

    private DecodedJWT getDecodedJwt(String jwtToken) {
        var algorithm = AlgorithmFactory.of(publicKey, jwtProperties.algorithmType());
        var verifier = JWT.require(algorithm).build();
        try {
            return verifier.verify(jwtToken);
        } catch (JWTVerificationException ex) {
            throw new InvalidJwtTokenException(
                "Exception while decoding JWT token",
                ex);
        }
    }

}
