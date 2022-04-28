package com.a6raywa1cher.jsonrestsecurity.providers;

import com.a6raywa1cher.jsonrestsecurity.authentication.JwtAuthentication;
import com.a6raywa1cher.jsonrestsecurity.component.authority.GrantedAuthorityService;
import com.a6raywa1cher.jsonrestsecurity.dao.model.IUser;
import com.a6raywa1cher.jsonrestsecurity.dao.service.IUserService;
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
@SuppressWarnings("ClassCanBeRecord")
public class JwtAuthenticationProvider implements AuthenticationProvider {
	private final IUserService<?> iUserService;
	private final BlockedRefreshTokensService blockedTokensService;
	private final GrantedAuthorityService grantedAuthorityService;

	public JwtAuthenticationProvider(IUserService<?> iUserService, BlockedRefreshTokensService blockedTokensService,
									 GrantedAuthorityService grantedAuthorityService) {
		this.iUserService = iUserService;
		this.blockedTokensService = blockedTokensService;
		this.grantedAuthorityService = grantedAuthorityService;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		if (!supports(authentication.getClass())) {
			return null;
		}
		try {
			JwtAuthentication customAuthentication = (JwtAuthentication) authentication;
			JwtToken jwtToken = customAuthentication.getCredentials();
			if (jwtToken == null) {
				throw new BadCredentialsException("JwtToken not provided");
			}
			Long userId = jwtToken.getUid();
			if (!blockedTokensService.isValid(userId, jwtToken.getRefreshId())) {
				throw new CredentialsExpiredException("Refresh-token was revoked");
			}
			IUser user = iUserService.getById(userId)
				.orElseThrow(() -> new UsernameNotFoundException(String.format("User %d doesn't exists", userId)));
			Collection<GrantedAuthority> authorities = grantedAuthorityService.getAuthorities(user);
			return new JwtAuthentication(authorities, jwtToken);
		} catch (Exception e) {
			authentication.setAuthenticated(false);
			throw e;
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return JwtAuthentication.class.isAssignableFrom(authentication);
	}
}
