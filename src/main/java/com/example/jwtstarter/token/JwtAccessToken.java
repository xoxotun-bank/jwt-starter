package com.example.jwtstarter.token;

import java.time.*;
import java.util.*;

import lombok.*;

@Builder
@Getter
public class JwtAccessToken {

    private Integer userId;

    private List<String> roles;

    private String city;

    private String region;

    private UUID jti;

    private ZonedDateTime expiredAt;

    private ZonedDateTime issuedAt;

}
