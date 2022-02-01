package com.a6raywa1cher.websecurityspringbootstarter.web;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.Duration;

@ConfigurationProperties("web-security")
@Validated
public class WebSecurityConfigProperties {
	@NotNull
	@Valid
	private JwtConfigProperties jwt = new JwtConfigProperties();

	@NotNull
	@Valid
	private CriticalActionLimiterConfigProperties criticalActionLimiter = new CriticalActionLimiterConfigProperties();

    private String[] corsAllowedOrigins = new String[0];

	private boolean enableDefaultWebConfig;

	private boolean enableAuthController;

    public WebSecurityConfigProperties() {
    }

    public @Valid JwtConfigProperties getJwt() {
        return this.jwt;
    }

    public void setJwt(@Valid JwtConfigProperties jwt) {
        this.jwt = jwt;
    }

    public @Valid CriticalActionLimiterConfigProperties getCriticalActionLimiter() {
        return this.criticalActionLimiter;
    }

    public void setCriticalActionLimiter(@Valid CriticalActionLimiterConfigProperties criticalActionLimiter) {
        this.criticalActionLimiter = criticalActionLimiter;
    }

    public String[] getCorsAllowedOrigins() {
        return this.corsAllowedOrigins;
    }

    public void setCorsAllowedOrigins(String[] corsAllowedOrigins) {
        this.corsAllowedOrigins = corsAllowedOrigins;
    }

    public boolean isEnableDefaultWebConfig() {
        return this.enableDefaultWebConfig;
    }

    public void setEnableDefaultWebConfig(boolean enableDefaultWebConfig) {
        this.enableDefaultWebConfig = enableDefaultWebConfig;
    }

    public boolean isEnableAuthController() {
        return this.enableAuthController;
    }

    public void setEnableAuthController(boolean enableAuthController) {
        this.enableAuthController = enableAuthController;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof WebSecurityConfigProperties)) return false;
        final WebSecurityConfigProperties other = (WebSecurityConfigProperties) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$jwt = this.getJwt();
        final Object other$jwt = other.getJwt();
        if (this$jwt == null ? other$jwt != null : !this$jwt.equals(other$jwt)) return false;
        final Object this$criticalActionLimiter = this.getCriticalActionLimiter();
        final Object other$criticalActionLimiter = other.getCriticalActionLimiter();
        if (this$criticalActionLimiter == null ? other$criticalActionLimiter != null : !this$criticalActionLimiter.equals(other$criticalActionLimiter))
            return false;
        if (!java.util.Arrays.deepEquals(this.getCorsAllowedOrigins(), other.getCorsAllowedOrigins())) return false;
        if (this.isEnableDefaultWebConfig() != other.isEnableDefaultWebConfig()) return false;
        if (this.isEnableAuthController() != other.isEnableAuthController()) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof WebSecurityConfigProperties;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $jwt = this.getJwt();
        result = result * PRIME + ($jwt == null ? 43 : $jwt.hashCode());
        final Object $criticalActionLimiter = this.getCriticalActionLimiter();
        result = result * PRIME + ($criticalActionLimiter == null ? 43 : $criticalActionLimiter.hashCode());
        result = result * PRIME + java.util.Arrays.deepHashCode(this.getCorsAllowedOrigins());
        result = result * PRIME + (this.isEnableDefaultWebConfig() ? 79 : 97);
        result = result * PRIME + (this.isEnableAuthController() ? 79 : 97);
        return result;
    }

    public String toString() {
        return "WebSecurityConfigProperties(jwt=" + this.getJwt() + ", criticalActionLimiter=" + this.getCriticalActionLimiter() + ", corsAllowedOrigins=" + java.util.Arrays.deepToString(this.getCorsAllowedOrigins()) + ", enableDefaultWebConfig=" + this.isEnableDefaultWebConfig() + ", enableAuthController=" + this.isEnableAuthController() + ")";
    }

    public static final class CriticalActionLimiterConfigProperties {
		private Boolean enable;

		@Positive
		private Integer maxAttempts;

		@NotNull
		private Duration blockDuration;

        public CriticalActionLimiterConfigProperties() {
        }

        public boolean isEnable() {
            return this.enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        public @Positive int getMaxAttempts() {
            return this.maxAttempts;
        }

        public void setMaxAttempts(@Positive int maxAttempts) {
            this.maxAttempts = maxAttempts;
        }

        public @NotNull Duration getBlockDuration() {
            return this.blockDuration;
        }

        public void setBlockDuration(@NotNull Duration blockDuration) {
            this.blockDuration = blockDuration;
        }

        public boolean equals(final Object o) {
            if (o == this) return true;
            if (!(o instanceof CriticalActionLimiterConfigProperties))
                return false;
            final CriticalActionLimiterConfigProperties other = (CriticalActionLimiterConfigProperties) o;
            if (this.isEnable() != other.isEnable()) return false;
            if (this.getMaxAttempts() != other.getMaxAttempts()) return false;
            final Object this$blockDuration = this.getBlockDuration();
            final Object other$blockDuration = other.getBlockDuration();
            if (this$blockDuration == null ? other$blockDuration != null : !this$blockDuration.equals(other$blockDuration))
                return false;
            return true;
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            result = result * PRIME + (this.isEnable() ? 79 : 97);
            result = result * PRIME + this.getMaxAttempts();
            final Object $blockDuration = this.getBlockDuration();
            result = result * PRIME + ($blockDuration == null ? 43 : $blockDuration.hashCode());
            return result;
        }

        public String toString() {
            return "WebSecurityConfigProperties.CriticalActionLimiterConfigProperties(enable=" + this.isEnable() + ", maxAttempts=" + this.getMaxAttempts() + ", blockDuration=" + this.getBlockDuration() + ")";
        }
    }

    public static final class JwtConfigProperties {
        @PositiveOrZero
        private int maxRefreshTokensPerUser;

        @NotBlank
        private String secret;

        @NotNull
        private Duration accessDuration;

        @NotNull
        private Duration refreshDuration;

        @NotNull
        private String issuerName;

        public JwtConfigProperties() {
        }

        public @PositiveOrZero int getMaxRefreshTokensPerUser() {
            return this.maxRefreshTokensPerUser;
        }

        public void setMaxRefreshTokensPerUser(@PositiveOrZero int maxRefreshTokensPerUser) {
            this.maxRefreshTokensPerUser = maxRefreshTokensPerUser;
        }

        public @NotBlank String getSecret() {
            return this.secret;
        }

        public void setSecret(@NotBlank String secret) {
            this.secret = secret;
        }

        public @NotNull Duration getAccessDuration() {
            return this.accessDuration;
        }

        public void setAccessDuration(@NotNull Duration accessDuration) {
            this.accessDuration = accessDuration;
        }

        public @NotNull Duration getRefreshDuration() {
            return this.refreshDuration;
        }

        public void setRefreshDuration(@NotNull Duration refreshDuration) {
            this.refreshDuration = refreshDuration;
        }

        public @NotNull String getIssuerName() {
            return this.issuerName;
        }

        public void setIssuerName(@NotNull String issuerName) {
            this.issuerName = issuerName;
        }

        public boolean equals(final Object o) {
            if (o == this) return true;
            if (!(o instanceof JwtConfigProperties))
                return false;
            final JwtConfigProperties other = (JwtConfigProperties) o;
            if (this.getMaxRefreshTokensPerUser() != other.getMaxRefreshTokensPerUser()) return false;
            final Object this$secret = this.getSecret();
            final Object other$secret = other.getSecret();
            if (this$secret == null ? other$secret != null : !this$secret.equals(other$secret)) return false;
            final Object this$accessDuration = this.getAccessDuration();
            final Object other$accessDuration = other.getAccessDuration();
            if (this$accessDuration == null ? other$accessDuration != null : !this$accessDuration.equals(other$accessDuration))
                return false;
            final Object this$refreshDuration = this.getRefreshDuration();
            final Object other$refreshDuration = other.getRefreshDuration();
            if (this$refreshDuration == null ? other$refreshDuration != null : !this$refreshDuration.equals(other$refreshDuration))
                return false;
            final Object this$issuerName = this.getIssuerName();
            final Object other$issuerName = other.getIssuerName();
            if (this$issuerName == null ? other$issuerName != null : !this$issuerName.equals(other$issuerName))
                return false;
            return true;
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            result = result * PRIME + this.getMaxRefreshTokensPerUser();
            final Object $secret = this.getSecret();
            result = result * PRIME + ($secret == null ? 43 : $secret.hashCode());
            final Object $accessDuration = this.getAccessDuration();
            result = result * PRIME + ($accessDuration == null ? 43 : $accessDuration.hashCode());
            final Object $refreshDuration = this.getRefreshDuration();
            result = result * PRIME + ($refreshDuration == null ? 43 : $refreshDuration.hashCode());
            final Object $issuerName = this.getIssuerName();
            result = result * PRIME + ($issuerName == null ? 43 : $issuerName.hashCode());
            return result;
        }

        public String toString() {
            return "WebSecurityConfigProperties.JwtConfigProperties(maxRefreshTokensPerUser=" + this.getMaxRefreshTokensPerUser() + ", secret=" + this.getSecret() + ", accessDuration=" + this.getAccessDuration() + ", refreshDuration=" + this.getRefreshDuration() + ", issuerName=" + this.getIssuerName() + ")";
        }
    }
}
