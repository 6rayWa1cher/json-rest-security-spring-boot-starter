package com.a6raywa1cher.websecurityspringbootstarter.rest;

import com.a6raywa1cher.websecurityspringbootstarter.component.WebSecurityComponentsConfiguration;
import com.a6raywa1cher.websecurityspringbootstarter.component.resolver.AuthenticationResolver;
import com.a6raywa1cher.websecurityspringbootstarter.dao.WebSecurityDaoConfiguration;
import com.a6raywa1cher.websecurityspringbootstarter.dao.service.UserService;
import com.a6raywa1cher.websecurityspringbootstarter.jwt.JwtAuthConfiguration;
import com.a6raywa1cher.websecurityspringbootstarter.jwt.service.JwtRefreshPairService;
import com.a6raywa1cher.websecurityspringbootstarter.jwt.service.RefreshTokenService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConditionalOnProperty(prefix = "web-security", value = "enable-auth-controller", havingValue = "true",
	matchIfMissing = true)
@Import({
	WebSecurityComponentsConfiguration.class,
	JwtAuthConfiguration.class,
	WebSecurityDaoConfiguration.class
})
public class AuthControllerConfiguration {
	@Bean
	public AuthController authController(
		AuthenticationResolver authenticationResolver,
		RefreshTokenService refreshTokenService,
		JwtRefreshPairService jwtRefreshPairService,
		UserService userService
	) {
		return new AuthController(authenticationResolver, refreshTokenService, jwtRefreshPairService, userService);
	}
}
