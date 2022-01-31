package com.a6raywa1cher.websecurityspringbootstarter.web;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@ConfigurationProperties("web-security")
@Validated
@Data
public class WebSecurityConfigProperties {
    @Valid
    private JwtConfigProperties jwt;

    @Valid
    private CriticalActionLimiterConfigProperties criticalActionLimiter;

    private String[] corsAllowedOrigins = new String[0];

    private boolean enableDefaultWebConfig = true;

    private boolean enableAuthController = true;

    @Data
    public static final class CriticalActionLimiterConfigProperties {
        private boolean enable = true;

        @Positive
        private int maxAttempts = 5;

        @NotNull
        private Duration blockDuration = Duration.of(1, ChronoUnit.MINUTES);
    }

    @Data
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
    }
}
