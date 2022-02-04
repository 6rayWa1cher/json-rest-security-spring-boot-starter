package com.a6raywa1cher.jsonrestsecurity.mainapp;

import com.a6raywa1cher.jsonrestsecurity.AbstractFullApplicationTests;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Collections;


@SpringBootTest(classes = MainTestApplication.class)
@ActiveProfiles("main")
class MainFullApplicationTests extends AbstractFullApplicationTests<User> {
	@Override
	public void createNewUser(String username, String password) {
		User user = new User();
		user.setUsername(username);
		user.setPassword(passwordEncoder.encode(password));
		user.setUserRole("USER");
		user.setLastVisitAt(LocalDateTime.now());
		user.setRefreshTokens(Collections.emptyList());
		userRepository.save(user);
	}
}
