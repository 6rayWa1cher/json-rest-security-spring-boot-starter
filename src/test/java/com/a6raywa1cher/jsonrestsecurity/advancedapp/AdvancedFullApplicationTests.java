package com.a6raywa1cher.jsonrestsecurity.advancedapp;

import com.a6raywa1cher.jsonrestsecurity.AbstractFullApplicationTests;
import com.a6raywa1cher.jsonrestsecurity.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Collections;

import static com.a6raywa1cher.jsonrestsecurity.TestUtils.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = AdvancedTestApplication.class)
@ActiveProfiles("advanced")
class AdvancedFullApplicationTests extends AbstractFullApplicationTests<User> {
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

	@Test
	void adminEndpointSecured() throws Exception {
		createNewUser(USERNAME, PASSWORD);
		String token = this.getJwtTokens(USERNAME, PASSWORD).getAccessToken();

		mvc.perform(get("/home/admin").header(TestUtils.AUTHORIZATION, jwt(token)))
			.andExpect(status().isForbidden());

		User user = userRepository.findByUsername(USERNAME).orElseThrow();
		user.setUserRole("ADMIN");
		userRepository.save(user);

		mvc.perform(get("/home/admin").header(TestUtils.AUTHORIZATION, jwt(token)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.admin").value("cookies"));
	}
}
