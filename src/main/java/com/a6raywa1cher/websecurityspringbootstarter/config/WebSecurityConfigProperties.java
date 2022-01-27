package com.a6raywa1cher.websecurityspringbootstarter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.Duration;

@ConfigurationProperties(prefix = "a6raywa1cher.web-auth")
@Validated
@Data
public class WebSecurityConfigProperties {
    @Valid
    private JwtConfigProperties jwt;

    private String[] corsAllowedOrigins = new String[0];

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
    }
}
