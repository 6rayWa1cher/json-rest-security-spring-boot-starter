package com.a6raywa1cher.jsonrestsecurity.component.resolver;

import com.a6raywa1cher.jsonrestsecurity.authentication.JwtAuthentication;
import com.a6raywa1cher.jsonrestsecurity.component.SecurityComponentsConfiguration;
import com.a6raywa1cher.jsonrestsecurity.dao.model.IUser;
import com.a6raywa1cher.jsonrestsecurity.dao.service.IUserService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * The default implementation of {@link AuthenticationResolver}.
 * <br/>
 * Resolves user from {@link JwtAuthentication} and {@link UsernamePasswordAuthenticationToken}.
 * <br/>
 *
 * @see AuthenticationResolver
 * @see SecurityComponentsConfiguration
 */
@SuppressWarnings("ClassCanBeRecord")
public class AuthenticationResolverImpl implements AuthenticationResolver {
	private final IUserService<?> IUserService;

	public AuthenticationResolverImpl(IUserService<?> IUserService) {
		this.IUserService = IUserService;
	}

	/**
	 * Returns the current user info from {@link SecurityContextHolder}
	 *
	 * @return current user
	 * @throws AuthenticationException        if user isn't logged in
	 * @throws AuthenticationResolveException unknown {@link Authentication}
	 * @see SecurityContextHolder
	 */
	@Override
	public IUser getUser() throws AuthenticationException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return getUser(authentication);
	}

	@Override
	public IUser getUser(Authentication authentication) throws AuthenticationException {
		if (authentication == null) {
			throw new BadCredentialsException("No credentials presented");
		}
		if (authentication instanceof JwtAuthentication jwtAuthentication) {
			return IUserService.getById(jwtAuthentication.getPrincipal()).orElseThrow();
		} else if (authentication instanceof UsernamePasswordAuthenticationToken token) {
			return IUserService.getById((Long) token.getPrincipal()).orElseThrow();
		}
		throw new AuthenticationResolveException("Unknown Authentication " + authentication.getClass().getCanonicalName());
	}
}
