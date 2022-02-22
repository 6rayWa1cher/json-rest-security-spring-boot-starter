package com.a6raywa1cher.jsonrestsecurity.component.authority;

import com.a6raywa1cher.jsonrestsecurity.component.SecurityComponentsConfiguration;
import com.a6raywa1cher.jsonrestsecurity.component.checker.UserEnabledChecker;
import com.a6raywa1cher.jsonrestsecurity.dao.model.IUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * The default implementation of {@link GrantedAuthorityService}.
 *
 * @see GrantedAuthorityService
 * @see UserEnabledChecker
 * @see SecurityComponentsConfiguration
 */
@SuppressWarnings("ClassCanBeRecord")
public class GrantedAuthorityServiceImpl implements GrantedAuthorityService {
	private final UserEnabledChecker userEnabledChecker;

	public GrantedAuthorityServiceImpl(UserEnabledChecker userEnabledChecker) {
		this.userEnabledChecker = userEnabledChecker;
	}

	/**
	 * Returns two types of authorities based on {@link SimpleGrantedAuthority}:
	 * <ul>
	 *     <li>{@code ROLE_${userRole}}</li>
	 *     <li>{@code ENABLED}, if {@link #userEnabledChecker} returns true</li>
	 * </ul>
	 *
	 * @param user the user, which granted authorities are requested
	 * @return granted authorities of the user
	 * @see UserEnabledChecker
	 */
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
