package com.a6raywa1cher.websecurityspringbootstarter.component;

import com.a6raywa1cher.websecurityspringbootstarter.authentication.CustomAuthenticationEntryPoint;
import com.a6raywa1cher.websecurityspringbootstarter.component.authority.GrantedAuthorityService;
import com.a6raywa1cher.websecurityspringbootstarter.component.authority.GrantedAuthorityServiceImpl;
import com.a6raywa1cher.websecurityspringbootstarter.component.checker.DefaultUserEnabledChecker;
import com.a6raywa1cher.websecurityspringbootstarter.component.checker.UserEnabledChecker;
import com.a6raywa1cher.websecurityspringbootstarter.component.resolver.AuthenticationResolver;
import com.a6raywa1cher.websecurityspringbootstarter.component.resolver.AuthenticationResolverImpl;
import com.a6raywa1cher.websecurityspringbootstarter.dao.service.UserService;
import com.a6raywa1cher.websecurityspringbootstarter.web.WebSecurityConfigProperties;
import com.a6raywa1cher.websecurityspringbootstarter.web.WebSecurityPropertiesConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@Import(WebSecurityPropertiesConfiguration.class)
public class WebSecurityComponentsConfiguration {
	@Bean
	@ConditionalOnMissingBean(CorsConfigurationSource.class)
	public CorsConfigurationSource corsConfigurationSource(WebSecurityConfigProperties properties) {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList(properties.getCorsAllowedOrigins()));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PATCH", "PUT", "HEAD", "OPTIONS"));
		configuration.setAllowedHeaders(Collections.singletonList("*"));
		configuration.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	@ConditionalOnBean(UserService.class)
	@ConditionalOnMissingBean(AuthenticationResolver.class)
	public AuthenticationResolver authenticationResolver(UserService userService) {
		return new AuthenticationResolverImpl(userService);
	}

	@Bean
	@ConditionalOnMissingBean(UserEnabledChecker.class)
	public UserEnabledChecker userEnabledChecker() {
		return new DefaultUserEnabledChecker();
	}

	@Bean
	@ConditionalOnMissingBean(GrantedAuthorityService.class)
	public GrantedAuthorityService grantedAuthorityService(UserEnabledChecker userEnabledChecker) {
		return new GrantedAuthorityServiceImpl(userEnabledChecker);
	}

	@Bean
	@ConditionalOnMissingBean(AuthenticationEntryPoint.class)
	public AuthenticationEntryPoint authenticationEntryPoint() {
		return new CustomAuthenticationEntryPoint();
	}
}
