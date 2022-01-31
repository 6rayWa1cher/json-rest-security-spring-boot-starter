package com.a6raywa1cher.websecurityspringbootstarter.jwt;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.OffsetDateTime;

public class JwtRefreshPair {
    private String refreshToken;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private OffsetDateTime refreshTokenExpiringAt;

    private String accessToken;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private OffsetDateTime accessTokenExpiringAt;

    public JwtRefreshPair(String refreshToken, OffsetDateTime refreshTokenExpiringAt, String accessToken, OffsetDateTime accessTokenExpiringAt) {
        this.refreshToken = refreshToken;
        this.refreshTokenExpiringAt = refreshTokenExpiringAt;
        this.accessToken = accessToken;
        this.accessTokenExpiringAt = accessTokenExpiringAt;
    }

    public String getRefreshToken() {
        return this.refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public OffsetDateTime getRefreshTokenExpiringAt() {
        return this.refreshTokenExpiringAt;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    public void setRefreshTokenExpiringAt(OffsetDateTime refreshTokenExpiringAt) {
        this.refreshTokenExpiringAt = refreshTokenExpiringAt;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public OffsetDateTime getAccessTokenExpiringAt() {
        return this.accessTokenExpiringAt;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    public void setAccessTokenExpiringAt(OffsetDateTime accessTokenExpiringAt) {
        this.accessTokenExpiringAt = accessTokenExpiringAt;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof JwtRefreshPair)) return false;
        final JwtRefreshPair other = (JwtRefreshPair) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$refreshToken = this.getRefreshToken();
        final Object other$refreshToken = other.getRefreshToken();
        if (this$refreshToken == null ? other$refreshToken != null : !this$refreshToken.equals(other$refreshToken))
            return false;
        final Object this$refreshTokenExpiringAt = this.getRefreshTokenExpiringAt();
        final Object other$refreshTokenExpiringAt = other.getRefreshTokenExpiringAt();
        if (this$refreshTokenExpiringAt == null ? other$refreshTokenExpiringAt != null : !this$refreshTokenExpiringAt.equals(other$refreshTokenExpiringAt))
            return false;
        final Object this$accessToken = this.getAccessToken();
        final Object other$accessToken = other.getAccessToken();
        if (this$accessToken == null ? other$accessToken != null : !this$accessToken.equals(other$accessToken))
            return false;
        final Object this$accessTokenExpiringAt = this.getAccessTokenExpiringAt();
        final Object other$accessTokenExpiringAt = other.getAccessTokenExpiringAt();
        if (this$accessTokenExpiringAt == null ? other$accessTokenExpiringAt != null : !this$accessTokenExpiringAt.equals(other$accessTokenExpiringAt))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof JwtRefreshPair;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $refreshToken = this.getRefreshToken();
        result = result * PRIME + ($refreshToken == null ? 43 : $refreshToken.hashCode());
        final Object $refreshTokenExpiringAt = this.getRefreshTokenExpiringAt();
        result = result * PRIME + ($refreshTokenExpiringAt == null ? 43 : $refreshTokenExpiringAt.hashCode());
        final Object $accessToken = this.getAccessToken();
        result = result * PRIME + ($accessToken == null ? 43 : $accessToken.hashCode());
        final Object $accessTokenExpiringAt = this.getAccessTokenExpiringAt();
        result = result * PRIME + ($accessTokenExpiringAt == null ? 43 : $accessTokenExpiringAt.hashCode());
        return result;
    }

    public String toString() {
        return "JwtRefreshPair(refreshToken=" + this.getRefreshToken() + ", refreshTokenExpiringAt=" + this.getRefreshTokenExpiringAt() + ", accessToken=" + this.getAccessToken() + ", accessTokenExpiringAt=" + this.getAccessTokenExpiringAt() + ")";
    }
}
