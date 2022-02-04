package com.a6raywa1cher.jsonrestsecurity.defaultapp;

import com.a6raywa1cher.jsonrestsecurity.AbstractFullApplicationTests;
import com.a6raywa1cher.jsonrestsecurity.dao.model.DefaultUser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Collections;


@SpringBootTest(classes = DefaultTestApplication.class)
@ActiveProfiles("default")
class DefaultFullApplicationTests extends AbstractFullApplicationTests<DefaultUser> {
	@Override
	public void createNewUser(String username, String password) {
		DefaultUser user = new DefaultUser();
		user.setUsername(username);
		user.setPassword(passwordEncoder.encode(password));
		user.setUserRole("USER");
		user.setLastVisitAt(LocalDateTime.now());
		user.setRefreshTokens(Collections.emptyList());
		userRepository.save(user);
	}
}
