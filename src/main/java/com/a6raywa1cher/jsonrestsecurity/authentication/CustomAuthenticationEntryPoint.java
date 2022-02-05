package com.a6raywa1cher.jsonrestsecurity.authentication;

import com.a6raywa1cher.jsonrestsecurity.component.SecurityComponentsConfiguration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * The default implementation of {@link AuthenticationEntryPoint}.
 * <br/>
 * It returns 401 if an exception is {@link BadCredentialsException} or {@link InsufficientAuthenticationException}
 * with the message {@code "Full authentication is required to access this resource"} else 403.
 * <br/>
 *
 * @see AuthenticationEntryPoint
 * @see SecurityComponentsConfiguration
 */
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
		if (authException instanceof BadCredentialsException ||
			(
				authException instanceof InsufficientAuthenticationException &&
					Objects.equals(authException.getMessage(), "Full authentication is required to access this resource")
			)
		) {
			response.setHeader("WWW-Authenticate", "FormBased");
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
		} else {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, authException.getMessage());
		}
	}
}
