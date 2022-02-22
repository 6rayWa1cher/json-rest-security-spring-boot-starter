package com.a6raywa1cher.jsonrestsecurity.component;

import com.a6raywa1cher.jsonrestsecurity.dao.service.UserService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.transaction.annotation.Transactional;

import static com.a6raywa1cher.jsonrestsecurity.utils.LogUtils.log;

@SuppressWarnings("ClassCanBeRecord")
public class FirstUserCreatorApplicationListener implements ApplicationListener<ApplicationReadyEvent> {
	private final String username;

	private final String password;

	private final String role;

	private final UserService<?> userService;

	public FirstUserCreatorApplicationListener(String username, String password, String role, UserService<?> userService) {
		this.username = username;
		this.password = password;
		this.role = role;
		this.userService = userService;
	}

	@Override
	@Transactional
	public void onApplicationEvent(ApplicationReadyEvent event) {
		if (userService.getByLogin(username).isEmpty()) {
			userService.create(username, password, role);
			log.info("Created admin-user");
		}
	}
}
