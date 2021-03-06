package com.a6raywa1cher.jsonrestsecurity.rest.req;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.Objects;

public class InvalidateTokenRequest {
	@NotNull
	@Size(min = 36, max = 36)
	private String refreshToken;

	@NotNull
	@Positive
	private Long userId;

	@SuppressWarnings("unused")
	public InvalidateTokenRequest(String refreshToken, Long userId) {
		this.refreshToken = refreshToken;
		this.userId = userId;
	}

	@SuppressWarnings("unused")
	public InvalidateTokenRequest() {
	}

	@Override
	public String toString() {
		return "InvalidateTokenRequest{" +
			"refreshToken='" + refreshToken + '\'' +
			", userId=" + userId +
			'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		InvalidateTokenRequest that = (InvalidateTokenRequest) o;
		return Objects.equals(refreshToken, that.refreshToken) && Objects.equals(userId, that.userId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(refreshToken, userId);
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
