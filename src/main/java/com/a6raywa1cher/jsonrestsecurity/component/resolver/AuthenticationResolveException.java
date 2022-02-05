package com.a6raywa1cher.jsonrestsecurity.component.resolver;

/**
 * General exception from {@link AuthenticationResolver}.
 * <br/>
 *
 * @see AuthenticationResolver
 */
public class AuthenticationResolveException extends RuntimeException {
	public AuthenticationResolveException(String message) {
		super(message);
	}
}
