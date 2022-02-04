package com.a6raywa1cher.jsonrestsecurity.component.authority;

import com.a6raywa1cher.jsonrestsecurity.component.checker.UserEnabledChecker;
import com.a6raywa1cher.jsonrestsecurity.dao.model.IUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class GrantedAuthorityServiceImpl implements GrantedAuthorityService {
	private final UserEnabledChecker userEnabledChecker;

	public GrantedAuthorityServiceImpl(UserEnabledChecker userEnabledChecker) {
		this.userEnabledChecker = userEnabledChecker;
	}

	@Override
	public Collection<GrantedAuthority> getAuthorities(IUser user) {
		Object userRole = user.getUserRole();
		Set<GrantedAuthority> authoritySet = new HashSet<>();
		authoritySet.add(new SimpleGrantedAuthority("ROLE_" + userRole.toString()));
		if (userEnabledChecker.check(user)) {
			authoritySet.add(new SimpleGrantedAuthority("ENABLED"));
		}
		return authoritySet;
	}
}
