package com.a6raywa1cher.jsonrestsecurity.faillimiter;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Scans incoming requests and outcoming responses and blocks clients with
 * too many authentication/authorization failures within a short period.
 * <br/>
 * If the user is already blocked in the {@link #service}, the filter will interrupt
 * the filter chain and return a 429 error.
 * Otherwise, it's going to check the result of the filter chain. If the response code
 * is 401 or 403, the {@link FailLimiterService#actionFailed} will be called.
 * <br/>
 *
 * @see FailLimiterService
 * @see FailLimiterConfiguration
 */
public class FailLimiterFilter extends OncePerRequestFilter {
	private final FailLimiterService service;
	private final String retryAfterSeconds;

	public FailLimiterFilter(FailLimiterService service, long retryAfterSeconds) {
		this.service = service;
		this.retryAfterSeconds = Long.toString(retryAfterSeconds);
	}

	@Override
	@SuppressWarnings("NullableProblems")
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String ip = request.getRemoteAddr();
		if (service.isBlocked(ip)) {
			response.setStatus(429);
			response.setHeader("Retry-After", retryAfterSeconds);
			SecurityContextHolder.clearContext();
		} else {
			filterChain.doFilter(request, response);
			if (response.getStatus() == HttpServletResponse.SC_FORBIDDEN ||
				response.getStatus() == HttpServletResponse.SC_UNAUTHORIZED) {
				service.actionFailed(ip);
			}
		}
	}
}
