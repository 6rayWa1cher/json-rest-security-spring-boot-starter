package com.a6raywa1cher.jsonrestsecurity.web;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.Duration;
import java.util.Arrays;
import java.util.Objects;

@ConfigurationProperties("json-rest-security")
@Validated
public class JsonRestSecurityConfigProperties {
	@NotNull
	@Valid
	private JwtConfigProperties jwt = new JwtConfigProperties();

	@NotNull
	@Valid
	private JsonRestSecurityConfigProperties.FailLimiterConfigProperties failLimiter = new FailLimiterConfigProperties();

	private String[] corsAllowedOrigins = new String[0];

	private boolean enableDefaultWebConfig;

	private boolean enableAuthController;

	private boolean enable;

	public JsonRestSecurityConfigProperties() {
	}

	@Override
	public String toString() {
		return "JsonRestSecurityConfigProperties{" +
			"jwt=" + jwt +
			", failLimiter=" + failLimiter +
			", corsAllowedOrigins=" + Arrays.toString(corsAllowedOrigins) +
			", enableDefaultWebConfig=" + enableDefaultWebConfig +
			", enableAuthController=" + enableAuthController +
			", enable=" + enable +
			'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		JsonRestSecurityConfigProperties that = (JsonRestSecurityConfigProperties) o;
		return enableDefaultWebConfig == that.enableDefaultWebConfig && enableAuthController == that.enableAuthController && enable == that.enable && Objects.equals(jwt, that.jwt) && Objects.equals(failLimiter, that.failLimiter) && Arrays.equals(corsAllowedOrigins, that.corsAllowedOrigins);
	}

	@Override
	public int hashCode() {
		int result = Objects.hash(jwt, failLimiter, enableDefaultWebConfig, enableAuthController, enable);
		result = 31 * result + Arrays.hashCode(corsAllowedOrigins);
		return result;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public @Valid JwtConfigProperties getJwt() {
		return this.jwt;
	}

	public void setJwt(@Valid JwtConfigProperties jwt) {
		this.jwt = jwt;
	}

	public @Valid JsonRestSecurityConfigProperties.FailLimiterConfigProperties getFailLimiter() {
		return this.failLimiter;
	}

	public void setFailLimiter(@Valid JsonRestSecurityConfigProperties.FailLimiterConfigProperties failLimiter) {
		this.failLimiter = failLimiter;
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

	public static final class FailLimiterConfigProperties {
		private Boolean enable;

		@Positive
		private Integer maxAttempts;

		@NotNull
		private Duration blockDuration;

		@NotNull
		private Integer maxCacheSize;

		public FailLimiterConfigProperties() {
		}

		@Override
		public String toString() {
			return "FailLimiterConfigProperties{" +
				"enable=" + enable +
				", maxAttempts=" + maxAttempts +
				", blockDuration=" + blockDuration +
				", maxCacheSize=" + maxCacheSize +
				'}';
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			FailLimiterConfigProperties that = (FailLimiterConfigProperties) o;
			return Objects.equals(enable, that.enable) && Objects.equals(maxAttempts, that.maxAttempts) && Objects.equals(blockDuration, that.blockDuration) && Objects.equals(maxCacheSize, that.maxCacheSize);
		}

		@Override
		public int hashCode() {
			return Objects.hash(enable, maxAttempts, blockDuration, maxCacheSize);
		}

		public Boolean getEnable() {
			return enable;
		}

		public void setEnable(Boolean enable) {
			this.enable = enable;
		}

		public Integer getMaxAttempts() {
			return maxAttempts;
		}

		public void setMaxAttempts(Integer maxAttempts) {
			this.maxAttempts = maxAttempts;
		}

		public Duration getBlockDuration() {
			return blockDuration;
		}

		public void setBlockDuration(Duration blockDuration) {
			this.blockDuration = blockDuration;
		}

		public Integer getMaxCacheSize() {
			return maxCacheSize;
		}

		public void setMaxCacheSize(Integer maxCacheSize) {
			this.maxCacheSize = maxCacheSize;
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
			return this$issuerName == null ? other$issuerName == null : this$issuerName.equals(other$issuerName);
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
