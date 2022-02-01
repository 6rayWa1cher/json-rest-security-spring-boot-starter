package com.a6raywa1cher.websecurityspringbootstarter.authentication;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
		if (authException instanceof BadCredentialsException) {
			response.setHeader("WWW-Authenticate", "FormBased");
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
		} else {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, authException.getMessage());
		}
//		} else {
//			response.setHeader("WWW-Authenticate", "FormBased");
//			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
//		}
	}
}
