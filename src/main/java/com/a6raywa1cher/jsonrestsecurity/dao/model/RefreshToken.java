package com.a6raywa1cher.jsonrestsecurity.dao.model;

import com.a6raywa1cher.jsonrestsecurity.dao.repo.RefreshTokenRepository;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Holds refresh token info.
 *
 * @see RefreshTokenRepository
 */
public class RefreshToken implements Cloneable {
	private String id;
	private String token;
	private LocalDateTime expiringAt;

	public RefreshToken() {
	}

	public RefreshToken(String id, String token, LocalDateTime expiringAt) {
		this.id = id;
		this.token = token;
		this.expiringAt = expiringAt;
	}

	@Override
	public RefreshToken clone() throws CloneNotSupportedException {
		return (RefreshToken) super.clone();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public LocalDateTime getExpiringAt() {
		return expiringAt;
	}

	public void setExpiringAt(LocalDateTime expiringAt) {
		this.expiringAt = expiringAt;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (RefreshToken) obj;
		return Objects.equals(this.id, that.id) &&
			Objects.equals(this.token, that.token) &&
			Objects.equals(this.expiringAt, that.expiringAt);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, token, expiringAt);
	}

	@Override
	public String toString() {
		return "RefreshToken[" +
			"id=" + id + ", " +
			"token=" + token + ", " +
			"expiringAt=" + expiringAt + ']';
	}

}
