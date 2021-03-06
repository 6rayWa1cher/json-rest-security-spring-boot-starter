package com.a6raywa1cher.jsonrestsecurity.web;

import com.a6raywa1cher.jsonrestsecurity.component.resolver.AuthenticationResolveException;
import com.a6raywa1cher.jsonrestsecurity.component.resolver.AuthenticationResolver;
import com.a6raywa1cher.jsonrestsecurity.dao.model.IUser;
import com.a6raywa1cher.jsonrestsecurity.dao.service.IUserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@SuppressWarnings({"unchecked", "NullableProblems"})
public class LastVisitFilter<T extends IUser> extends OncePerRequestFilter {
	private final IUserService<T> iUserService;
	private final AuthenticationResolver resolver;

	public LastVisitFilter(IUserService<T> iUserService, AuthenticationResolver resolver) {
		this.iUserService = iUserService;
		this.resolver = resolver;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
		} finally {
			try {
				if (SecurityContextHolder.getContext().getAuthentication() != null) {
					T user = (T) resolver.getUser();
					LocalDateTime lastVisit = user.getLastVisitAt();
					if (lastVisit == null || user.getLastVisitAt().plusSeconds(30).isBefore(LocalDateTime.now())) {
						iUserService.updateLastVisitAt(user);
					}
				}
			} catch (AuthenticationResolveException ignored) {

			} catch (Exception e) {
				logger.error("Error while setting last visit", e);
			}
		}
	}
}
