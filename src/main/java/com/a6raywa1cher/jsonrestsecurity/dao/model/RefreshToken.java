package com.a6raywa1cher.jsonrestsecurity.dao.model;

import java.time.LocalDateTime;

/**
 * Holds refresh token info.
 *
 * @see com.a6raywa1cher.jsonrestsecurity.dao.repo.RefreshTokenRepository
 */
public record RefreshToken(String id, String token, LocalDateTime expiringAt) implements Cloneable {
	@Override
	public RefreshToken clone() throws CloneNotSupportedException {
		return (RefreshToken) super.clone();
	}
}
