package com.a6raywa1cher.jsonrestsecurity.component.resolver;


import com.a6raywa1cher.jsonrestsecurity.dao.model.IUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Provides the user info from the {@link Authentication}.
 *
 * @see Authentication
 * @see AuthenticationResolverImpl
 */
public interface AuthenticationResolver {
	/**
	 * Returns the user info from environment (usually from {@link SecurityContextHolder})
	 *
	 * @return current user
	 * @throws AuthenticationException        if user isn't logged in
	 * @throws AuthenticationResolveException general error
	 */
	IUser getUser() throws AuthenticationException;

	/**
	 * Returns the user info from {@link Authentication}
	 *
	 * @param authentication an {@link Authentication} object
	 * @return current user
	 * @throws AuthenticationException        if user isn't logged in
	 * @throws AuthenticationResolveException general error
	 */
	IUser getUser(Authentication authentication) throws AuthenticationException;
}
