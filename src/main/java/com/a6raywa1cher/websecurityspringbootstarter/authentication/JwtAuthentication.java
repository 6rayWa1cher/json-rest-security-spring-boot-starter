package com.a6raywa1cher.websecurityspringbootstarter.authentication;

import com.a6raywa1cher.websecurityspringbootstarter.jwt.JwtToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;

public class JwtAuthentication implements Authentication {
	private final Collection<? extends GrantedAuthority> authorities;
	private final Long userId;
	private final JwtToken credentials;
	private boolean authenticated;

	public JwtAuthentication(JwtToken jwtToken) {
		this.authorities = Collections.emptyList();
		this.credentials = jwtToken;
		this.userId = null;
		this.authenticated = false;
	}

	public JwtAuthentication(Collection<? extends GrantedAuthority> authorities, JwtToken jwtToken) {
		this.authorities = authorities;
		this.credentials = jwtToken;
		this.userId = jwtToken.getUid();
		this.authenticated = true;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public JwtToken getCredentials() {
		return credentials;
	}

	@Override
	public Object getDetails() {
		return null;
	}

	@Override
	public Long getPrincipal() {
		return userId;
	}

	@Override
	public boolean isAuthenticated() {
		return authenticated;
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		if (isAuthenticated) {
			throw new IllegalArgumentException();
		}
		authenticated = false;
	}

	@Override
	public String getName() {
		return Long.toString(userId);
	}
}
