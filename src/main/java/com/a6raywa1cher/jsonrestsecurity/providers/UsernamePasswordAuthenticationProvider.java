package com.a6raywa1cher.jsonrestsecurity.providers;

import com.a6raywa1cher.jsonrestsecurity.component.authority.GrantedAuthorityService;
import com.a6raywa1cher.jsonrestsecurity.dao.model.IUser;
import com.a6raywa1cher.jsonrestsecurity.dao.service.UserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.Optional;

public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {
	private final PasswordEncoder passwordEncoder;
	private final UserService userService;
	private final GrantedAuthorityService grantedAuthorityService;

	public UsernamePasswordAuthenticationProvider(UserService userService, PasswordEncoder passwordEncoder,
												  GrantedAuthorityService grantedAuthorityService) {
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
		this.grantedAuthorityService = grantedAuthorityService;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		if (!(authentication instanceof UsernamePasswordAuthenticationToken token) ||
				!(authentication.getPrincipal() instanceof String) ||
				!(authentication.getCredentials() instanceof String inputPassword)) {
			return null;
		}
        String principal = (String) token.getPrincipal();
        Optional<IUser> byUsername = userService.getByLogin(principal);
		if (byUsername.isEmpty()) {
			throw new BadCredentialsException("User not exists or incorrect password");
		}
        IUser user = byUsername.get();
		if (user.getPassword() == null || "".equals(user.getPassword())) {
			throw new DisabledException("User didn't set up password");
		}
		if (!passwordEncoder.matches(inputPassword, user.getPassword())) {
			throw new BadCredentialsException("User not exists or incorrect password");
		}
		Collection<GrantedAuthority> authorities = grantedAuthorityService.getAuthorities(user);
		return new UsernamePasswordAuthenticationToken(
				user.getId(), token, authorities);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
