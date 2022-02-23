package com.a6raywa1cher.jsonrestsecurity.component;

import com.a6raywa1cher.jsonrestsecurity.authentication.CustomAuthenticationEntryPoint;
import com.a6raywa1cher.jsonrestsecurity.component.authority.GrantedAuthorityService;
import com.a6raywa1cher.jsonrestsecurity.component.authority.GrantedAuthorityServiceImpl;
import com.a6raywa1cher.jsonrestsecurity.component.checker.DefaultUserEnabledChecker;
import com.a6raywa1cher.jsonrestsecurity.component.checker.UserEnabledChecker;
import com.a6raywa1cher.jsonrestsecurity.component.resolver.AuthenticationResolver;
import com.a6raywa1cher.jsonrestsecurity.component.resolver.AuthenticationResolverImpl;
import com.a6raywa1cher.jsonrestsecurity.dao.service.IUserService;
import com.a6raywa1cher.jsonrestsecurity.web.JsonRestSecurityConfigProperties;
import com.a6raywa1cher.jsonrestsecurity.web.JsonRestSecurityPropertiesConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

/**
 * Configures all beans from {@link com.a6raywa1cher.jsonrestsecurity.component} package.
 */
@Configuration
@Import(JsonRestSecurityPropertiesConfiguration.class)
@SuppressWarnings("SpringFacetCodeInspection")
public class SecurityComponentsConfiguration {
	private final JsonRestSecurityConfigProperties.FirstUserConfigProperties userConfigProperties;

	public SecurityComponentsConfiguration(JsonRestSecurityConfigProperties configProperties) {
		this.userConfigProperties = configProperties.getFirstUser();
	}

	/**
	 * Returns default CORS settings bean based on {@code json-rest-security.cors-allowed-origins} properties
	 *
	 * @param properties holder of allowed origins
	 * @return CORS settings bean
	 * @see JsonRestSecurityConfigProperties#getCorsAllowedOrigins()
	 */
	@Bean
	@ConditionalOnMissingBean(CorsConfigurationSource.class)
	public CorsConfigurationSource corsConfigurationSource(JsonRestSecurityConfigProperties properties) {
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
	@ConditionalOnBean(IUserService.class)
	@ConditionalOnMissingBean(AuthenticationResolver.class)
	public AuthenticationResolver authenticationResolver(IUserService<?> IUserService) {
		return new AuthenticationResolverImpl(IUserService);
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

	@Bean
	@ConditionalOnMissingBean(FirstUserCreatorApplicationListener.class)
	@ConditionalOnProperty(prefix = "json-rest-security.first-user", value = "enable", havingValue = "true")
	public FirstUserCreatorApplicationListener firstAdminCreatorApplicationListener(IUserService<?> IUserService) {
		return new FirstUserCreatorApplicationListener(
			userConfigProperties.getUsername(),
			userConfigProperties.getPassword(),
			userConfigProperties.getRole(),
			IUserService
		);
	}
}
