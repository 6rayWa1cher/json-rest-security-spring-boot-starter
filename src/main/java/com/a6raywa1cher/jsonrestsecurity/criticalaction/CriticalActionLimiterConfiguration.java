package com.a6raywa1cher.jsonrestsecurity.criticalaction;

import com.a6raywa1cher.jsonrestsecurity.web.JsonRestSecurityConfigProperties;
import com.a6raywa1cher.jsonrestsecurity.web.JsonRestSecurityPropertiesConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

@Configuration
@ConditionalOnProperty(prefix = "web-security.critical-action-limiter", name = "enable", havingValue = "true",
	matchIfMissing = true)
@Import(JsonRestSecurityPropertiesConfiguration.class)
public class CriticalActionLimiterConfiguration {
	private final JsonRestSecurityConfigProperties.CriticalActionLimiterConfigProperties properties;

	public CriticalActionLimiterConfiguration(JsonRestSecurityConfigProperties properties) {
		this.properties = properties.getCriticalActionLimiter();
	}

	@Bean
	public CriticalActionLimiterService criticalActionLimiterService() {
		return new CriticalActionLimiterService(properties.getMaxAttempts(), properties.getBlockDuration());
	}

    @Bean
    @Order(HIGHEST_PRECEDENCE)
    public CriticalActionLimiterFilter criticalActionLimiterFilter(CriticalActionLimiterService service) {
        return new CriticalActionLimiterFilter(service, properties.getBlockDuration().toSeconds());
    }
}
