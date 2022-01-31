package com.a6raywa1cher.websecurityspringbootstarter.component.criticalaction;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

@Configuration
@ConditionalOnProperty(prefix = "web-security.critical-action-limiter", name = "enable")
@EnableConfigurationProperties(CriticalActionLimiterConfigProperties.class)
public class CriticalActionLimiterAutoConfiguration {
    @Bean
    public CriticalActionLimiterService criticalActionLimiterService(CriticalActionLimiterConfigProperties properties) {
        return new CriticalActionLimiterService(properties.getMaxAttempts(), properties.getBlockDuration());
    }

    @Bean
    @Order(HIGHEST_PRECEDENCE)
    public CriticalActionLimiterFilter criticalActionLimiterFilter(
            CriticalActionLimiterService service,
            CriticalActionLimiterConfigProperties properties
    ) {
        return new CriticalActionLimiterFilter(service, properties.getBlockDuration().toSeconds());
    }
}
