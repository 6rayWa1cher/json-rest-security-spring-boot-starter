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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;

/**
 * Authenticates the client with {@link UsernamePasswordAuthenticationToken}.
 * <br/>
 * Steps of the provider:
 * <ol>
 *     <li>Load the user. If it's not found - throw {@link UsernameNotFoundException}</li>
 *     <li>Check if user has provided a password. If isn't - throw {@link DisabledException}</li>
 *     <li>Check the password. If password isn't matches - throw {@link BadCredentialsException}</li>
 *     <li>Gather granted authorities from {@link #grantedAuthorityService} for the user</li>
 *     <li>Return authenticated {@link UsernamePasswordAuthenticationToken}</li>
 * </ol>
 *
 * @see com.a6raywa1cher.jsonrestsecurity.web.JsonRestWebSecurityConfigurer
 */
@SuppressWarnings("ClassCanBeRecord")
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {
	private final PasswordEncoder passwordEncoder;
	private final UserService<?> userService;
	private final GrantedAuthorityService grantedAuthorityService;

	public UsernamePasswordAuthenticationProvider(UserService<?> userService, PasswordEncoder passwordEncoder,
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
		try {
			String principal = (String) token.getPrincipal();
			IUser user = userService.getByLogin(principal)
				.orElseThrow(() -> new UsernameNotFoundException("User not exists or incorrect password"));
			if (user.getPassword() == null || "".equals(user.getPassword())) {
				throw new DisabledException("User didn't set up password");
			}
			if (!passwordEncoder.matches(inputPassword, user.getPassword())) {
				throw new BadCredentialsException("User not exists or incorrect password");
			}
			Collection<GrantedAuthority> authorities = grantedAuthorityService.getAuthorities(user);
			return new UsernamePasswordAuthenticationToken(
				user.getId(), token, authorities);
		} catch (Exception e) {
			authentication.setAuthenticated(false);
			throw e;
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
