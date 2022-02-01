package com.a6raywa1cher.websecurityspringbootstarter.jwt;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.OffsetDateTime;
import java.util.Objects;

public class JwtRefreshPair {
	private String refreshToken;

	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private OffsetDateTime refreshTokenExpiringAt;

	private String accessToken;

	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private OffsetDateTime accessTokenExpiringAt;

	private Long userId;

	public JwtRefreshPair(String refreshToken, OffsetDateTime refreshTokenExpiringAt, String accessToken, OffsetDateTime accessTokenExpiringAt, Long userId) {
		this.refreshToken = refreshToken;
		this.refreshTokenExpiringAt = refreshTokenExpiringAt;
		this.accessToken = accessToken;
		this.accessTokenExpiringAt = accessTokenExpiringAt;
		this.userId = userId;
	}

	public JwtRefreshPair() {
	}

	@Override
	public String toString() {
		return "JwtRefreshPair{" +
			"refreshToken='" + refreshToken + '\'' +
			", refreshTokenExpiringAt=" + refreshTokenExpiringAt +
			", accessToken='" + accessToken + '\'' +
			", accessTokenExpiringAt=" + accessTokenExpiringAt +
			", userId=" + userId +
			'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		JwtRefreshPair that = (JwtRefreshPair) o;
		return Objects.equals(refreshToken, that.refreshToken) && Objects.equals(refreshTokenExpiringAt, that.refreshTokenExpiringAt) && Objects.equals(accessToken, that.accessToken) && Objects.equals(accessTokenExpiringAt, that.accessTokenExpiringAt) && Objects.equals(userId, that.userId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(refreshToken, refreshTokenExpiringAt, accessToken, accessTokenExpiringAt, userId);
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public OffsetDateTime getRefreshTokenExpiringAt() {
		return refreshTokenExpiringAt;
	}

	public void setRefreshTokenExpiringAt(OffsetDateTime refreshTokenExpiringAt) {
		this.refreshTokenExpiringAt = refreshTokenExpiringAt;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public OffsetDateTime getAccessTokenExpiringAt() {
		return accessTokenExpiringAt;
	}

	public void setAccessTokenExpiringAt(OffsetDateTime accessTokenExpiringAt) {
		this.accessTokenExpiringAt = accessTokenExpiringAt;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
