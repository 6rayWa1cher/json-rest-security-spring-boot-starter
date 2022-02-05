package com.a6raywa1cher.jsonrestsecurity.jwt.service;

import com.a6raywa1cher.jsonrestsecurity.jwt.JwtToken;

import java.util.Optional;

/**
 * Provides two main methods to operate JWT tokens
 *
 * @see com.a6raywa1cher.jsonrestsecurity.jwt.service.impl.JwtTokenServiceImpl
 */
public interface JwtTokenService {
	/**
	 * Issues a new signed JWT token and returns it in {@link JwtToken}.
	 *
	 * @param userId    user id
	 * @param refreshId associated refresh token id
	 * @return new JWT token
	 */
	JwtToken issue(Long userId, String refreshId);

	/**
	 * Decodes JWT token, validates signature and expiry timestamp and
	 * returns extracted data.
	 *
	 * @param token JWT token
	 * @return JwtToken with the extracted data or an empty optional if the token is invalid or expired
	 */
	Optional<JwtToken> decode(String token);
}
