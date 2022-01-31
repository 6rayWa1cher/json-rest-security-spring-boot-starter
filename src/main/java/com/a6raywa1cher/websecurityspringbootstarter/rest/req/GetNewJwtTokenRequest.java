package com.a6raywa1cher.websecurityspringbootstarter.rest.req;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class GetNewJwtTokenRequest {
    @NotNull
    @Size(min = 36, max = 36)
    private String refreshToken;

    public GetNewJwtTokenRequest() {
    }

    public @NotNull @Size(min = 36, max = 36) String getRefreshToken() {
        return this.refreshToken;
    }

    public void setRefreshToken(@NotNull @Size(min = 36, max = 36) String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof GetNewJwtTokenRequest)) return false;
        final GetNewJwtTokenRequest other = (GetNewJwtTokenRequest) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$refreshToken = this.getRefreshToken();
        final Object other$refreshToken = other.getRefreshToken();
        if (this$refreshToken == null ? other$refreshToken != null : !this$refreshToken.equals(other$refreshToken))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof GetNewJwtTokenRequest;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $refreshToken = this.getRefreshToken();
        result = result * PRIME + ($refreshToken == null ? 43 : $refreshToken.hashCode());
        return result;
    }

    public String toString() {
        return "GetNewJwtTokenRequest(refreshToken=" + this.getRefreshToken() + ")";
    }
}
