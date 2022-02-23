package com.a6raywa1cher.jsonrestsecurity.rest;

import com.a6raywa1cher.jsonrestsecurity.component.SecurityComponentsConfiguration;
import com.a6raywa1cher.jsonrestsecurity.component.resolver.AuthenticationResolver;
import com.a6raywa1cher.jsonrestsecurity.dao.DaoConfiguration;
import com.a6raywa1cher.jsonrestsecurity.dao.service.IUserService;
import com.a6raywa1cher.jsonrestsecurity.jwt.JwtAuthConfiguration;
import com.a6raywa1cher.jsonrestsecurity.jwt.service.JwtRefreshPairService;
import com.a6raywa1cher.jsonrestsecurity.jwt.service.RefreshTokenService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConditionalOnProperty(prefix = "web-security", value = "enable-auth-controller", havingValue = "true",
	matchIfMissing = true)
@Import({
	SecurityComponentsConfiguration.class,
	JwtAuthConfiguration.class,
	DaoConfiguration.class
})
@SuppressWarnings("SpringFacetCodeInspection")
public class AuthControllerConfiguration {
	@Bean
	public AuthController authController(
		AuthenticationResolver authenticationResolver,
		RefreshTokenService refreshTokenService,
		JwtRefreshPairService jwtRefreshPairService,
		IUserService<?> IUserService
	) {
		return new AuthController(authenticationResolver, refreshTokenService, jwtRefreshPairService, IUserService);
	}
}
