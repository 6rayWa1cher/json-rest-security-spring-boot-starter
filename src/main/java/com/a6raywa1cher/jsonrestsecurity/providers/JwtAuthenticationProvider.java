package com.a6raywa1cher.jsonrestsecurity.providers;

import com.a6raywa1cher.jsonrestsecurity.authentication.JwtAuthentication;
import com.a6raywa1cher.jsonrestsecurity.component.authority.GrantedAuthorityService;
import com.a6raywa1cher.jsonrestsecurity.dao.model.IUser;
import com.a6raywa1cher.jsonrestsecurity.dao.service.UserService;
import com.a6raywa1cher.jsonrestsecurity.jwt.JwtToken;
import com.a6raywa1cher.jsonrestsecurity.jwt.service.BlockedRefreshTokensService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.Optional;

/**
 * Authenticates the client with {@link JwtAuthentication}.
 * <br/>
 * Steps of the provider:
 * <ol>
 *     <li>Extract the {@link JwtToken} from authentication. If it's null - throw {@link BadCredentialsException}</li>
 *     <li>Check if the associated refresh token was recently revoked in {@link #blockedTokensService}. If it's revoked - throw {@link CredentialsExpiredException}</li>
 *     <li>Load the user. If it's not found - throw {@link UsernameNotFoundException}</li>
 *     <li>Gather granted authorities from {@link #grantedAuthorityService} for the user</li>
 *     <li>Return authenticated {@link JwtAuthentication}</li>
 * </ol>
 *
 * @see com.a6raywa1cher.jsonrestsecurity.web.JsonRestWebSecurityConfigurer
 */
public class JwtAuthenticationProvider implements AuthenticationProvider {
	private final UserService userService;
	private final BlockedRefreshTokensService blockedTokensService;
	private final GrantedAuthorityService grantedAuthorityService;

	public JwtAuthenticationProvider(UserService userService, BlockedRefreshTokensService blockedTokensService,
									 GrantedAuthorityService grantedAuthorityService) {
		this.userService = userService;
		this.blockedTokensService = blockedTokensService;
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
		if (!blockedTokensService.isValid(jwtToken.getRefreshId())) {
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
