package com.a6raywa1cher.websecurityspringbootstarter.rest;

import com.a6raywa1cher.websecurityspringbootstarter.component.resolver.AuthenticationResolver;
import com.a6raywa1cher.websecurityspringbootstarter.dao.service.UserService;
import com.a6raywa1cher.websecurityspringbootstarter.jwt.service.JwtRefreshPairService;
import com.a6raywa1cher.websecurityspringbootstarter.jwt.service.RefreshTokenService;
import com.a6raywa1cher.websecurityspringbootstarter.web.WebSecurityAutoConfiguration;
import com.a6raywa1cher.websecurityspringbootstarter.web.WebSecurityConfigProperties;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "web-security", value = "enable-auth-controller")
@AutoConfigureAfter(WebSecurityAutoConfiguration.class)
@EnableConfigurationProperties(WebSecurityConfigProperties.class)
public class AuthControllerAutoConfiguration {
    @Bean
    @ConditionalOnBean({
            AuthenticationResolver.class, RefreshTokenService.class, JwtRefreshPairService.class, UserService.class
    })
    public AuthController authController(
            AuthenticationResolver authenticationResolver,
            RefreshTokenService refreshTokenService,
            JwtRefreshPairService jwtRefreshPairService,
            UserService userService
    ) {
        return new AuthController(authenticationResolver, refreshTokenService, jwtRefreshPairService, userService);
    }
}
