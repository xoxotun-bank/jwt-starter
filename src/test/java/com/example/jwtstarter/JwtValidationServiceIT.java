package com.example.jwtstarter;

import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.test.context.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.*;

import plugin.npca.jwt.configuration.*;
import plugin.npca.jwt.token.*;

@SpringBootTest(classes = {JwtStarterAutoConfiguration.class})
@ActiveProfiles("dev")
public class JwtValidationServiceIT {

    @Autowired
    private JwtValidationService jwtValidationService;

    @Autowired
    private JwtProperties jwtProperties;

    @Test
    public void invalidKey() {
        var key = "invalidKey";
        var ex = assertThrows(RuntimeException.class, () ->
            JwtValidationService.buildPubKey(key, jwtProperties.algorithmFamily()));

        var expectedMessage = "Error on building public key(RSA): invalidKey";
        assertEquals(expectedMessage, ex.getMessage());
    }

    @ParameterizedTest
    @CsvSource(
        value = {"RSA256, RS256",
                 "RSA512, RS512",
                 "RSA384, RS384"}
    )
    public void correctKey(String algorithmType, String algorithmName) {
        var key = JwtValidationService.buildPubKey(
            jwtProperties.publicKey(),
            jwtProperties.algorithmFamily());

        var algorithm = AlgorithmFactory.of(key, algorithmType);
        assertEquals(algorithmName, algorithm.getName());
    }

    @Test
    @DisplayName("Validate token RSA256")
    public void validateToken() {
        var sourceToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJ1c2VyX2lkIjoxLCJjaXR5IjoiZXhhbXBsZSIsInJvbGVzIjpbIm9wZXJhdG9yIl0sImV4cCI6MTc0OTQ2NTUyNiwicmVnaW9uIjoiZXhhbXBsZSIsImlhdCI6MTcxOTQ2NTUyNiwianRpIjoiODNiYWIwNDAtZGQ5NC00NGExLThiZDMtMTQwYmZmNzFmN2VmIn0.UeYAdLr0L0QcJk0ezNmAnmDtW45bDG_EXueBxipeQldocbfsdUkM0n4qX0ubPhILKSaOGiW3W_2mr3UVVRGUmyAJaZgAIP5-9B9Y5KBkOGfh_UWjs_YsvlYh3idChes_zTeaCXogLsi_kfrdYCrHMOAVZ56He37wxIzgw1LdYx4DrcmBgStW_ResT13w7Julmt-raqDRf2T9bMvpPyJfhq_khO4e9uzrbAOFuxUJ6nNMD6oyEeRNJMt_rNjhY9wYZsyfnAlKdbz1BqJaZ-RH7mRthxSq2ULc51Uo-w3ps57H_gCEgBw6BP4kd85S6ctCLV8bgj2Lkzkuw8D2kxneCQ";
        var expectedCityAndRegion = "example";
        var expectedRole = List.of("operator");
        var expectedUserId = 1;

        JwtAccessToken model = jwtValidationService.extractAccessTokenPayload(sourceToken);
        assertEquals(expectedCityAndRegion, model.getCity());
        assertEquals(expectedCityAndRegion, model.getRegion());
        assertEquals(expectedRole.size(), model.getRoles().size());
        assertTrue(expectedRole.containsAll(model.getRoles()));
        assertEquals(expectedUserId, model.getUserId());

    }

}
