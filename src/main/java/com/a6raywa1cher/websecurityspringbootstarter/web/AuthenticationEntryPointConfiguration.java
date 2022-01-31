package com.a6raywa1cher.websecurityspringbootstarter.web;

import com.a6raywa1cher.websecurityspringbootstarter.authentication.CustomAuthenticationEntryPoint;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.AuthenticationEntryPoint;

@Configuration
@AutoConfigureBefore(SecurityAutoConfiguration.class)
@ConditionalOnMissingBean(AuthenticationEntryPoint.class)
public class AuthenticationEntryPointConfiguration {
    @Bean
    @ConditionalOnMissingBean(AuthenticationEntryPoint.class)
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }
}