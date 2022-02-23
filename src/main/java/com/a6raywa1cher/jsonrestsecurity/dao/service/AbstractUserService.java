package com.a6raywa1cher.jsonrestsecurity.dao.service;

import com.a6raywa1cher.jsonrestsecurity.dao.model.IUser;
import com.a6raywa1cher.jsonrestsecurity.dao.repo.IUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * The abstract implementation of {@link IUserService}.
 *
 * @see DefaultUserService
 */
public abstract class AbstractUserService<T extends IUser> implements IUserService<T> {
	protected final IUserRepository<T> userRepository;
	protected final PasswordEncoder passwordEncoder;

	public AbstractUserService(IUserRepository<T> userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void updateLastVisitAt(T user) {
		user.setLastVisitAt(LocalDateTime.now());
		userRepository.save(user);
	}

	@Override
	public Optional<T> getById(Long id) {
		return userRepository.findById(id);
	}

	@Override
	public Optional<T> getByLogin(String login) {
		return userRepository.findByUsername(login);
	}

	@Override
	public Optional<T> getByLoginAndPassword(String username, String rawPassword) {
		Optional<T> optional = userRepository.findByUsername(username);
		return optional.filter(u -> passwordEncoder.matches(rawPassword, u.getPassword()));
	}

	public abstract T create(String login, String rawPassword, String role);
}
