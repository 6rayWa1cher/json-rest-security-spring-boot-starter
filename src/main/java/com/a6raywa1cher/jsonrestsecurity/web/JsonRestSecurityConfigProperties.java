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

	@NotNull
	@Valid
	private JsonRestSecurityConfigProperties.FirstUserConfigProperties firstUser = new FirstUserConfigProperties();

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
			", firstUser=" + firstUser +
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
		return enableDefaultWebConfig == that.enableDefaultWebConfig && enableAuthController == that.enableAuthController && enable == that.enable && Objects.equals(jwt, that.jwt) && Objects.equals(failLimiter, that.failLimiter) && Objects.equals(firstUser, that.firstUser) && Arrays.equals(corsAllowedOrigins, that.corsAllowedOrigins);
	}

	@Override
	public int hashCode() {
		int result = Objects.hash(jwt, failLimiter, firstUser, enableDefaultWebConfig, enableAuthController, enable);
		result = 31 * result + Arrays.hashCode(corsAllowedOrigins);
		return result;
	}

	public FirstUserConfigProperties getFirstUser() {
		return firstUser;
	}

	public void setFirstUser(FirstUserConfigProperties firstUser) {
		this.firstUser = firstUser;
	}

	public JwtConfigProperties getJwt() {
		return jwt;
	}

	public void setJwt(JwtConfigProperties jwt) {
		this.jwt = jwt;
	}

	public FailLimiterConfigProperties getFailLimiter() {
		return failLimiter;
	}

	public void setFailLimiter(FailLimiterConfigProperties failLimiter) {
		this.failLimiter = failLimiter;
	}

	public String[] getCorsAllowedOrigins() {
		return corsAllowedOrigins;
	}

	public void setCorsAllowedOrigins(String[] corsAllowedOrigins) {
		this.corsAllowedOrigins = corsAllowedOrigins;
	}

	public boolean isEnableDefaultWebConfig() {
		return enableDefaultWebConfig;
	}

	public void setEnableDefaultWebConfig(boolean enableDefaultWebConfig) {
		this.enableDefaultWebConfig = enableDefaultWebConfig;
	}

	public boolean isEnableAuthController() {
		return enableAuthController;
	}

	public void setEnableAuthController(boolean enableAuthController) {
		this.enableAuthController = enableAuthController;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
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

		@Override
		public String toString() {
			return "JwtConfigProperties{" +
				"maxRefreshTokensPerUser=" + maxRefreshTokensPerUser +
				", secret='" + secret + '\'' +
				", accessDuration=" + accessDuration +
				", refreshDuration=" + refreshDuration +
				", issuerName='" + issuerName + '\'' +
				'}';
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			JwtConfigProperties that = (JwtConfigProperties) o;
			return maxRefreshTokensPerUser == that.maxRefreshTokensPerUser && Objects.equals(secret, that.secret) && Objects.equals(accessDuration, that.accessDuration) && Objects.equals(refreshDuration, that.refreshDuration) && Objects.equals(issuerName, that.issuerName);
		}

		@Override
		public int hashCode() {
			return Objects.hash(maxRefreshTokensPerUser, secret, accessDuration, refreshDuration, issuerName);
		}

		public int getMaxRefreshTokensPerUser() {
			return maxRefreshTokensPerUser;
		}

		public void setMaxRefreshTokensPerUser(int maxRefreshTokensPerUser) {
			this.maxRefreshTokensPerUser = maxRefreshTokensPerUser;
		}

		public String getSecret() {
			return secret;
		}

		public void setSecret(String secret) {
			this.secret = secret;
		}

		public Duration getAccessDuration() {
			return accessDuration;
		}

		public void setAccessDuration(Duration accessDuration) {
			this.accessDuration = accessDuration;
		}

		public Duration getRefreshDuration() {
			return refreshDuration;
		}

		public void setRefreshDuration(Duration refreshDuration) {
			this.refreshDuration = refreshDuration;
		}

		public String getIssuerName() {
			return issuerName;
		}

		public void setIssuerName(String issuerName) {
			this.issuerName = issuerName;
		}
	}

	public static final class FirstUserConfigProperties {
		private boolean enable;

		private String username;

		private String password;

		private String role;

		public FirstUserConfigProperties() {
		}

		@Override
		public String toString() {
			return "FirstUserConfigProperties{" +
				"enable=" + enable +
				", username='" + username + '\'' +
				", password='" + password + '\'' +
				", role='" + role + '\'' +
				'}';
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			FirstUserConfigProperties that = (FirstUserConfigProperties) o;
			return enable == that.enable && Objects.equals(username, that.username) && Objects.equals(password, that.password) && Objects.equals(role, that.role);
		}

		@Override
		public int hashCode() {
			return Objects.hash(enable, username, password, role);
		}

		public boolean isEnable() {
			return enable;
		}

		public void setEnable(boolean enable) {
			this.enable = enable;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getRole() {
			return role;
		}

		public void setRole(String role) {
			this.role = role;
		}
	}
}
