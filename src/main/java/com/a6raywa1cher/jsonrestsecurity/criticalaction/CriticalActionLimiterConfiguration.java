package com.a6raywa1cher.jsonrestsecurity.criticalaction;

import com.a6raywa1cher.jsonrestsecurity.web.JsonRestSecurityConfigProperties;
import com.a6raywa1cher.jsonrestsecurity.web.JsonRestSecurityPropertiesConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

/**
 * Configures all beans from {@link com.a6raywa1cher.jsonrestsecurity.criticalaction} package.
 * <br/>
 * CriticalActionLimiter can be disabled by property {@code web-security.critical-action-limiter.enable=false}
 */
@Configuration
@ConditionalOnProperty(prefix = "web-security.critical-action-limiter", name = "enable", havingValue = "true",
	matchIfMissing = true)
@Import(JsonRestSecurityPropertiesConfiguration.class)
public class CriticalActionLimiterConfiguration {
	private final JsonRestSecurityConfigProperties.CriticalActionLimiterConfigProperties properties;

	public CriticalActionLimiterConfiguration(JsonRestSecurityConfigProperties properties) {
		this.properties = properties.getCriticalActionLimiter();
	}

	/**
	 * Returns the {@link CriticalActionLimiterService} bean.
	 * <br/>
	 * Max attempts are being taken from {@code web-security.critical-action-limiter.max-attempts} property (by default 5).
	 * <br/>
	 * The block duration is being taken from {@code web-security.critical-action-limiter.block-duration} property (by default 1 minute).
	 *
	 * @return CriticalActionLimiterService bean
	 */
	@Bean
	public CriticalActionLimiterService criticalActionLimiterService() {
		return new CriticalActionLimiterService(properties.getMaxAttempts(), properties.getBlockDuration());
	}

	/**
	 * Returns the {@link CriticalActionLimiterFilter} bean.
	 * <br/>
	 * The block duration is being taken from {@code web-security.critical-action-limiter.block-duration} property (by default 1 minute).
	 *
	 * @return CriticalActionLimiterFilter bean
	 */
	@Bean
	@Order(HIGHEST_PRECEDENCE)
	public CriticalActionLimiterFilter criticalActionLimiterFilter(CriticalActionLimiterService service) {
		return new CriticalActionLimiterFilter(service, properties.getBlockDuration().toSeconds());
	}
}
