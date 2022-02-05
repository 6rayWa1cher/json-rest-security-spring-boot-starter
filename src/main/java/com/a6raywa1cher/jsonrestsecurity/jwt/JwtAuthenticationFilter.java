package com.a6raywa1cher.jsonrestsecurity.jwt;

import com.a6raywa1cher.jsonrestsecurity.authentication.JwtAuthentication;
import com.a6raywa1cher.jsonrestsecurity.jwt.service.JwtTokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * Provides the access token authentication hook for a Spring WebMvc application.
 * <br/>
 * The filter looks for the authentication header in the request and checks if auth type is {@code jwt} or
 * {@code bearer}. Then it creates a non-authenticated {@link JwtAuthentication} with {@link JwtTokenService} assist
 * and invokes {@link AuthenticationManager} for further process. If authentication is successful, the filter
 * injects the authenticated {@link JwtAuthentication} into {@link SecurityContextHolder}.
 * <br/>
 *
 * @see com.a6raywa1cher.jsonrestsecurity.web.JsonRestWebSecurityConfigurer
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private static final String AUTHORIZATION_HEADER = "Authorization";
	private final JwtTokenService jwtTokenService;
	private final AuthenticationManager authenticationManager;

	public JwtAuthenticationFilter(JwtTokenService jwtTokenService, AuthenticationManager authenticationManager) {
		this.jwtTokenService = jwtTokenService;
		this.authenticationManager = authenticationManager;
	}

	/**
	 * Extracts the non-authenticated {@link JwtAuthentication} from request.
	 * <br/>
	 * Supports only {@code jwt} and {@code bearer} prefixes.
	 *
	 * @param request  HttpServletRequest instance
	 * @param response HttpServletResponse instance
	 * @return JwtAuthentication or null
	 * @throws AuthenticationException if the token was found but {@link #jwtTokenService} couldn't extract/validate it.
	 */
	private Authentication check(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		if (request.getHeader(AUTHORIZATION_HEADER) == null) {
			return null;
		}
		String lowerCase = request.getHeader(AUTHORIZATION_HEADER).toLowerCase();
		String token;
		if (lowerCase.startsWith("jwt ") || lowerCase.startsWith("bearer ")) {
			token = request.getHeader(AUTHORIZATION_HEADER).split(" ")[1];
		} else {
			return null;
		}
		Optional<JwtToken> jwtBody = jwtTokenService.decode(token);
		if (jwtBody.isEmpty()) {
			throw new BadCredentialsException("Broken JWT or an unknown sign key");
		} else {
			return new JwtAuthentication(jwtBody.get());
		}
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		try {
			Authentication authentication = check(request, response);
			if (authentication != null) {
				Authentication auth = authenticationManager.authenticate(authentication);
				SecurityContextHolder.createEmptyContext();
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		} catch (AuthenticationException e) {
			SecurityContextHolder.clearContext();
			logger.debug("AuthenticationException on jwt", e);
			response.setStatus(403);
			return;
		}
		filterChain.doFilter(request, response);
	}
}