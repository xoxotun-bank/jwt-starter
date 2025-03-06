package com.example.jwtstarter;

import java.security.*;
import java.security.interfaces.*;
import java.util.*;

import com.auth0.jwt.algorithms.*;
import lombok.experimental.*;

@UtilityClass
public class AlgorithmFactory {

    public static Algorithm of(RSAPublicKey rsaPublicKey, String algorithmType) {
        validateInputParameters(rsaPublicKey, algorithmType);
        return switch (algorithmType) {
            case "RSA256" -> Algorithm.RSA256(rsaPublicKey, null);
            case "RSA384" -> Algorithm.RSA384(rsaPublicKey, null);
            case "RSA512" -> Algorithm.RSA512(rsaPublicKey, null);
            default -> throw new IllegalArgumentException("Unsupported algorithm type");
        };
    }

    public static Algorithm of(RSAPrivateKey rsaPrivateKey, String algorithmType) {
        validateInputParameters(rsaPrivateKey, algorithmType);
        return switch (algorithmType) {
            case "RSA256" -> Algorithm.RSA256(null, rsaPrivateKey);
            case "RSA384" -> Algorithm.RSA384(null, rsaPrivateKey);
            case "RSA512" -> Algorithm.RSA512(null, rsaPrivateKey);
            default -> throw new IllegalArgumentException("Unsupported algorithm type");
        };
    }

    private static void validateInputParameters(Key key, String algorithmType) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(algorithmType);
    }

}
