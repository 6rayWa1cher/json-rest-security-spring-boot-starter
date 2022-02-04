package com.a6raywa1cher.jsonrestsecurity.criticalaction;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CriticalActionLimiterFilter extends OncePerRequestFilter {
    private final CriticalActionLimiterService service;
    private final String retryAfterSeconds;

    public CriticalActionLimiterFilter(CriticalActionLimiterService service, long retryAfterSeconds) {
        this.service = service;
        this.retryAfterSeconds = Long.toString(retryAfterSeconds);
    }

    @Override
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
			} else {
				service.actionSucceed(ip);
			}
		}
    }
}
