package com.a6raywa1cher.websecurityspringbootstarter.criticalaction;

import com.a6raywa1cher.websecurityspringbootstarter.web.WebSecurityConfigProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

@Configuration
@ConditionalOnProperty(prefix = "web-security.critical-action-limiter", name = "enable")
@EnableConfigurationProperties(WebSecurityConfigProperties.class)
public class CriticalActionLimiterAutoConfiguration {
    private final WebSecurityConfigProperties.CriticalActionLimiterConfigProperties properties;

    public CriticalActionLimiterAutoConfiguration(WebSecurityConfigProperties properties) {
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
