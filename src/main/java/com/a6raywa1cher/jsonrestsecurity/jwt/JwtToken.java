package com.a6raywa1cher.jsonrestsecurity.jwt;

import java.time.LocalDateTime;

public class JwtToken {
	private String token;

	private LocalDateTime expiringAt;

	private long uid;

	private String refreshId;

	JwtToken(String token, LocalDateTime expiringAt, long uid, String refreshId) {
		this.token = token;
		this.expiringAt = expiringAt;
		this.uid = uid;
		this.refreshId = refreshId;
	}

	public static JwtTokenBuilder builder() {
		return new JwtTokenBuilder();
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public LocalDateTime getExpiringAt() {
		return this.expiringAt;
	}

	public void setExpiringAt(LocalDateTime expiringAt) {
		this.expiringAt = expiringAt;
	}

	public long getUid() {
		return this.uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public String getRefreshId() {
		return this.refreshId;
	}

	public void setRefreshId(String refreshId) {
		this.refreshId = refreshId;
	}

	public boolean equals(final Object o) {
		if (o == this) return true;
		if (!(o instanceof JwtToken)) return false;
		final JwtToken other = (JwtToken) o;
		if (!other.canEqual(this)) return false;
		final Object this$token = this.getToken();
		final Object other$token = other.getToken();
		if (this$token == null ? other$token != null : !this$token.equals(other$token)) return false;
		final Object this$expiringAt = this.getExpiringAt();
		final Object other$expiringAt = other.getExpiringAt();
		if (this$expiringAt == null ? other$expiringAt != null : !this$expiringAt.equals(other$expiringAt))
			return false;
		if (this.getUid() != other.getUid()) return false;
		final Object this$refreshId = this.getRefreshId();
		final Object other$refreshId = other.getRefreshId();
		return this$refreshId == null ? other$refreshId == null : this$refreshId.equals(other$refreshId);
	}

	protected boolean canEqual(final Object other) {
		return other instanceof JwtToken;
	}

	public int hashCode() {
		final int PRIME = 59;
		int result = 1;
		final Object $token = this.getToken();
		result = result * PRIME + ($token == null ? 43 : $token.hashCode());
		final Object $expiringAt = this.getExpiringAt();
		result = result * PRIME + ($expiringAt == null ? 43 : $expiringAt.hashCode());
		final long $uid = this.getUid();
		result = result * PRIME + (int) ($uid >>> 32 ^ $uid);
		final Object $refreshId = this.getRefreshId();
		result = result * PRIME + ($refreshId == null ? 43 : $refreshId.hashCode());
		return result;
	}

	public String toString() {
		return "JwtToken(token=" + this.getToken() + ", expiringAt=" + this.getExpiringAt() + ", uid=" + this.getUid() + ", refreshId=" + this.getRefreshId() + ")";
	}

	public static class JwtTokenBuilder {
		private String token;
		private LocalDateTime expiringAt;
		private long uid;
		private String refreshId;

		JwtTokenBuilder() {
		}

		public JwtTokenBuilder token(String token) {
			this.token = token;
			return this;
		}

		public JwtTokenBuilder expiringAt(LocalDateTime expiringAt) {
			this.expiringAt = expiringAt;
			return this;
		}

		public JwtTokenBuilder uid(long uid) {
			this.uid = uid;
			return this;
		}

		public JwtTokenBuilder refreshId(String refreshId) {
			this.refreshId = refreshId;
			return this;
		}

		public JwtToken build() {
			return new JwtToken(token, expiringAt, uid, refreshId);
		}

		public String toString() {
			return "JwtToken.JwtTokenBuilder(token=" + this.token + ", expiringAt=" + this.expiringAt + ", uid=" + this.uid + ", refreshId=" + this.refreshId + ")";
		}
	}
}
