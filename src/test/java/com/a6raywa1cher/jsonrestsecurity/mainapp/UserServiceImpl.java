package com.a6raywa1cher.jsonrestsecurity.mainapp;

import com.a6raywa1cher.jsonrestsecurity.dao.repo.IUserRepository;
import com.a6raywa1cher.jsonrestsecurity.dao.service.AbstractUserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserServiceImpl extends AbstractUserService<User> {
	public UserServiceImpl(IUserRepository<User> userRepository, PasswordEncoder passwordEncoder) {
		super(userRepository, passwordEncoder);
	}

	@Override
	public User create(String login, String rawPassword, String role) {
		User user = new User();
		user.setUsername(login);
		user.setPassword(passwordEncoder.encode(rawPassword));
		user.setUserRole(role);
		return userRepository.save(user);
	}
}
