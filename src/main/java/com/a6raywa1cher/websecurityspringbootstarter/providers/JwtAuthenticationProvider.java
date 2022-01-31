package com.a6raywa1cher.websecurityspringbootstarter.providers;

import com.a6raywa1cher.websecurityspringbootstarter.authentication.JwtAuthentication;
import com.a6raywa1cher.websecurityspringbootstarter.component.authority.GrantedAuthorityService;
import com.a6raywa1cher.websecurityspringbootstarter.dao.model.IUser;
import com.a6raywa1cher.websecurityspringbootstarter.dao.service.UserService;
import com.a6raywa1cher.websecurityspringbootstarter.jwt.JwtToken;
import com.a6raywa1cher.websecurityspringbootstarter.jwt.service.BlockedRefreshTokensService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.Optional;

public class JwtAuthenticationProvider implements AuthenticationProvider {
	private final UserService userService;
	private final BlockedRefreshTokensService service;
	private final GrantedAuthorityService grantedAuthorityService;

	public JwtAuthenticationProvider(UserService userService, BlockedRefreshTokensService service,
									 GrantedAuthorityService grantedAuthorityService) {
		this.userService = userService;
		this.service = service;
		this.grantedAuthorityService = grantedAuthorityService;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		if (!supports(authentication.getClass())) {
			return null;
		}
		JwtAuthentication customAuthentication = (JwtAuthentication) authentication;
		JwtToken jwtToken = customAuthentication.getCredentials();
		if (jwtToken == null) {
			customAuthentication.setAuthenticated(false);
			throw new BadCredentialsException("JwtToken not provided");
		}
		if (!service.isValid(jwtToken.getRefreshId())) {
			throw new CredentialsExpiredException("Refresh-token was revoked");
		}
        Long userId = jwtToken.getUid();
        Optional<IUser> byId = userService.getById(userId);
		if (byId.isEmpty()) {
			customAuthentication.setAuthenticated(false);
			throw new UsernameNotFoundException(String.format("User %d doesn't exists", userId));
		}
        IUser user = byId.get();
        Collection<GrantedAuthority> authorities = grantedAuthorityService.getAuthorities(user);
		return new JwtAuthentication(authorities, jwtToken);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return JwtAuthentication.class.isAssignableFrom(authentication);
	}
}
