package com.a6raywa1cher.websecurityspringbootstarter.jwt.service.impl;

import com.a6raywa1cher.websecurityspringbootstarter.jwt.JwtToken;
import com.a6raywa1cher.websecurityspringbootstarter.jwt.service.JwtTokenService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;

public class JwtTokenServiceImpl implements JwtTokenService {
    private final static String REFRESH_TOKEN_ID_CLAIM = "rti";
    private final String issuerName;
    private final String secret;
    private final Duration duration;
    private Algorithm algorithm;
    private JWTVerifier jwtVerifier;

    public JwtTokenServiceImpl(String issuerName, String secret, Duration duration) {
        this.secret = secret;
        this.duration = duration;
        this.issuerName = issuerName;
    }

    @PostConstruct
    public void init() {
        algorithm = Algorithm.HMAC512(secret);
        jwtVerifier = JWT.require(algorithm)
                .withIssuer(issuerName)
                .build();
    }

    @Override
    public JwtToken issue(Long userId, String refreshId) {
        ZonedDateTime expiringAt = nowPlusDuration();
        String token = JWT.create()
                .withIssuer(issuerName)
                .withSubject(Long.toString(userId))
                .withExpiresAt(Date.from(expiringAt.toInstant()))
                .withClaim(REFRESH_TOKEN_ID_CLAIM, refreshId)
                .sign(algorithm);
        return JwtToken.builder()
                .token(token)
                .uid(userId)
                .expiringAt(expiringAt.toLocalDateTime())
                .build();
    }

    private ZonedDateTime nowPlusDuration() {
        return ZonedDateTime.now().plus(duration);
    }

    @Override
    public Optional<JwtToken> decode(String token) {
        try {
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            JwtToken.JwtTokenBuilder builder = JwtToken.builder()
                    .token(token)
                    .uid(Long.parseLong(decodedJWT.getSubject()))
                    .expiringAt(decodedJWT.getExpiresAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                    .refreshId(decodedJWT.getClaim(REFRESH_TOKEN_ID_CLAIM).asString());
            return Optional.of(builder.build());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
