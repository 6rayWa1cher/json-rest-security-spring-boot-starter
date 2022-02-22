package com.a6raywa1cher.jsonrestsecurity.dao.service;

import com.a6raywa1cher.jsonrestsecurity.dao.DaoConfiguration;
import com.a6raywa1cher.jsonrestsecurity.dao.model.DefaultUser;
import com.a6raywa1cher.jsonrestsecurity.dao.repo.IUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * The default implementation of {@link AbstractUserService}
 *
 * @see DaoConfiguration
 */
public class DefaultUserService extends AbstractUserService<DefaultUser> {
	public DefaultUserService(IUserRepository<DefaultUser> userRepository, PasswordEncoder passwordEncoder) {
		super(userRepository, passwordEncoder);
	}

	@Override
	public DefaultUser create(String login, String rawPassword, String role) {
		DefaultUser defaultUser = new DefaultUser();
		defaultUser.setUsername(login);
		defaultUser.setPassword(passwordEncoder.encode(rawPassword));
		defaultUser.setUserRole(role);
		return userRepository.save(defaultUser);
	}
}
