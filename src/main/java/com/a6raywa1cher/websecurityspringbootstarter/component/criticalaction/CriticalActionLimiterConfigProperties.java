package com.a6raywa1cher.websecurityspringbootstarter.component.criticalaction;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@ConfigurationProperties("web-security.critical-action-limiter")
@Validated
@Data
public class CriticalActionLimiterConfigProperties {
    private boolean enable = true;

    @Positive
    private int maxAttempts = 5;

    @NotNull
    private Duration blockDuration = Duration.of(1, ChronoUnit.MINUTES);
}
