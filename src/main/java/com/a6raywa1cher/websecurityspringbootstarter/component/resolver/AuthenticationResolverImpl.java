package com.a6raywa1cher.websecurityspringbootstarter.component.resolver;

import com.a6raywa1cher.websecurityspringbootstarter.authentication.JwtAuthentication;
import com.a6raywa1cher.websecurityspringbootstarter.jpa.model.AbstractUser;
import com.a6raywa1cher.websecurityspringbootstarter.jpa.service.UserService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationResolverImpl implements AuthenticationResolver {
	private final UserService userService;

	public AuthenticationResolverImpl(UserService userService) {
		this.userService = userService;
	}

	@Override
	public AbstractUser getUser() throws AuthenticationException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return getUser(authentication);
	}

	@Override
	public AbstractUser getUser(Authentication authentication) throws AuthenticationException {
		if (authentication == null) {
			throw new BadCredentialsException("No credentials presented");
		}
		if (authentication instanceof JwtAuthentication jwtAuthentication) {
			return userService.getById(jwtAuthentication.getPrincipal()).orElseThrow();
		} else if (authentication instanceof UsernamePasswordAuthenticationToken token) {
			return userService.getById((Long) token.getPrincipal()).orElseThrow();
		}
		throw new AuthenticationResolveException("Unknown Authentication " + authentication.getClass().getCanonicalName());
	}
}
