package com.a6raywa1cher.jsonrestsecurity.jwt.service.impl;

import com.a6raywa1cher.jsonrestsecurity.jwt.JwtToken;
import com.a6raywa1cher.jsonrestsecurity.jwt.service.JwtTokenService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;

/**
 * The default implementation of {@link JwtTokenService}.
 * <br/>
 * Works on {@link com.auth0.jwt.JWT} by auth0. Uses HMAC512 signature algorithm.
 * <br/>
 * Example of the issued JWT (decoded):
 * <br/>
 * Header:
 * <code>
 * <pre>
 * {
 *   "typ": "JWT",
 *   "alg": "HS512"
 * }
 * </pre>
 * </code>
 * Payload:
 * <code>
 * <pre>
 * {
 *   "sub": "1",
 *   "rti": "38f726e6-756c-4bd7-8352-78e8cd6017d0",
 *   "iss": "test-app",
 *   "exp": 1644070560
 * }
 * </pre>
 * </code>
 * In payload {@code rti} is the refresh token id.
 *
 * @see com.a6raywa1cher.jsonrestsecurity.jwt.JwtAuthConfiguration
 */
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
		return new JwtToken(token, expiringAt.toLocalDateTime(), userId, refreshId);
	}

	private ZonedDateTime nowPlusDuration() {
		return ZonedDateTime.now().plus(duration);
	}

	@Override
	public Optional<JwtToken> decode(String token) {
		try {
			DecodedJWT decodedJWT = jwtVerifier.verify(token);
			long userId = Long.parseLong(decodedJWT.getSubject());
			LocalDateTime expiringAt = decodedJWT.getExpiresAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			String refreshId = decodedJWT.getClaim(REFRESH_TOKEN_ID_CLAIM).asString();
			return Optional.of(new JwtToken(token, expiringAt, userId, refreshId));
		} catch (Exception e) {
			return Optional.empty();
		}
	}
}
