package com.a6raywa1cher.jsonrestsecurity.jwt;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Contains all useful information from JWT token
 *
 * @see com.a6raywa1cher.jsonrestsecurity.jwt.service.JwtTokenService
 */
public class JwtToken {
	private String token;

	private LocalDateTime expiringAt;

	private long uid;

	private String refreshId;

	public JwtToken(String token, LocalDateTime expiringAt, long uid, String refreshId) {
		this.token = token;
		this.expiringAt = expiringAt;
		this.uid = uid;
		this.refreshId = refreshId;
	}

	@Override
	public String toString() {
		return "JwtToken{" +
			"token='" + token + '\'' +
			", expiringAt=" + expiringAt +
			", uid=" + uid +
			", refreshId='" + refreshId + '\'' +
			'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		JwtToken jwtToken = (JwtToken) o;
		return uid == jwtToken.uid && Objects.equals(token, jwtToken.token) && Objects.equals(expiringAt, jwtToken.expiringAt) && Objects.equals(refreshId, jwtToken.refreshId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(token, expiringAt, uid, refreshId);
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

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public String getRefreshId() {
		return refreshId;
	}

	public void setRefreshId(String refreshId) {
		this.refreshId = refreshId;
	}
}
