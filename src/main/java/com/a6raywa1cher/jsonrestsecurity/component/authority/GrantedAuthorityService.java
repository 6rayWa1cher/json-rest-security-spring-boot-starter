package com.a6raywa1cher.jsonrestsecurity.component.authority;

import com.a6raywa1cher.jsonrestsecurity.dao.model.IUser;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Provides the list of {@link GrantedAuthority} owned by the user.
 * <br/>
 * Used by custom authentication providers to calculate granted authorities of users.
 *
 * @see GrantedAuthorityServiceImpl
 * @see com.a6raywa1cher.jsonrestsecurity.providers.JwtAuthenticationProvider
 * @see com.a6raywa1cher.jsonrestsecurity.providers.UsernamePasswordAuthenticationProvider
 */
public interface GrantedAuthorityService {
	/**
	 * Returns the granted authorities of the user.
	 *
	 * @param user the user, which granted authorities are requested
	 * @return granted authorities of the user
	 */
	Collection<GrantedAuthority> getAuthorities(IUser user);
}
