package com.a6raywa1cher.jsonrestsecurity.faillimiter;

import com.a6raywa1cher.jsonrestsecurity.web.JsonRestSecurityConfigProperties;
import com.a6raywa1cher.jsonrestsecurity.web.JsonRestSecurityPropertiesConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

/**
 * Configures all beans from {@link com.a6raywa1cher.jsonrestsecurity.faillimiter} package.
 * <br/>
 * FailLimiter can be disabled by property {@code json-rest-security.fail-limiter.enable=false}
 */
@Configuration
@ConditionalOnProperty(prefix = "json-rest-security.fail-limiter", name = "enable", havingValue = "true",
	matchIfMissing = true)
@Import(JsonRestSecurityPropertiesConfiguration.class)
@SuppressWarnings("SpringFacetCodeInspection")
public class FailLimiterConfiguration {
	private final JsonRestSecurityConfigProperties.FailLimiterConfigProperties properties;

	public FailLimiterConfiguration(JsonRestSecurityConfigProperties properties) {
		this.properties = properties.getFailLimiter();
	}

	/**
	 * Returns the {@link FailLimiterService} bean with {@link LoadingCacheFailLimiterService} implementation.
	 * <br/>
	 * Max attempts are being taken from {@code json-rest-security.fail-limiter.max-attempts} property (by default 5).
	 * <br/>
	 * The block duration is being taken from {@code json-rest-security.fail-limiter.block-duration} property (by default 1 minute).
	 * <br/>
	 * The max cache size is being taken from {@code json-rest-security.fail-limiter.max-cache-size} property (by default 30000).
	 *
	 * @return FailLimiterService bean
	 */
	@Bean
	@ConditionalOnMissingBean(FailLimiterService.class)
	public FailLimiterService failLimiterService() {
		return new LoadingCacheFailLimiterService(properties.getMaxAttempts(), properties.getBlockDuration(), properties.getMaxCacheSize());
	}

	/**
	 * Returns the {@link FailLimiterFilter} bean.
	 * <br/>
	 * The block duration is being taken from {@code json-rest-security.fail-limiter.block-duration} property (by default 1 minute).
	 *
	 * @return FailLimiterFilter bean
	 */
	@Bean
	@Order(HIGHEST_PRECEDENCE)
	public FailLimiterFilter failLimiterFilter(FailLimiterService service) {
		return new FailLimiterFilter(service, properties.getBlockDuration().toSeconds());
	}
}
