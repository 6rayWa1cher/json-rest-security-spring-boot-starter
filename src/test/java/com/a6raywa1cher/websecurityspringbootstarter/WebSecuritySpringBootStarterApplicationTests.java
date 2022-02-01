package com.a6raywa1cher.websecurityspringbootstarter;

import com.a6raywa1cher.websecurityspringbootstarter.web.WebSecurityAutoConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;

import static com.a6raywa1cher.websecurityspringbootstarter.TestUtils.basic;
import static com.a6raywa1cher.websecurityspringbootstarter.TestUtils.jwt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(classes = TestApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class WebSecuritySpringBootStarterApplicationTests {
	static final String AUTHORIZATION = "Authorization";
	//	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
//		.withUserConfiguration(TestAppConfiguration.class)
//		.withPropertyValues(
//			"web-security.jwt.secret", "meow",
//			"web-security.jwt.refresh-duration", "P1D",
//			"web-security.jwt.access-duration", "PT5M",
//			"web-security.jwt.issuer-name", "test-app",
//			""
//		);
	@Autowired
	ApplicationContext ctx;

	@Autowired
	MockMvc mvc;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	ObjectMapper objectMapper;

	@Test
	void contextLoads() {

	}

	@Test
	void beansArePresent() {
		assertThat(ctx.containsBean("authController")).isTrue();
		assertThat(ctx.getBeanNamesForType(WebSecurityAutoConfiguration.WebSecurityConfigurer.class)).hasSize(1);
		assertThat(ctx.containsBean("criticalActionLimiterFilter")).isTrue();
	}

	@Nested
	public class WithRegisteredUserTests {
		final String USERNAME = "abcdef";
		final String PASSWORD = "qwerty";

		@BeforeEach
		void createUser() {
			User user = new User();
			user.setUsername(USERNAME);
			user.setPassword(passwordEncoder.encode(PASSWORD));
			user.setUserRole("USER");
			user.setLastVisitAt(LocalDateTime.now());
			user.setRefreshTokens(Collections.emptyList());
			userRepository.save(user);
		}

		@Test
		@Transactional
		void basicLoginPassesValidUser() throws Exception {
			mvc.perform(
					get("/home/")
						.header(AUTHORIZATION, basic(USERNAME, PASSWORD))
				)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.cookies").value(true));
		}

		@Test
		@Transactional
		void basicLoginBlocksInvalidUser() throws Exception {
			mvc.perform(
					get("/home/")
						.header(AUTHORIZATION, basic(USERNAME, PASSWORD + "1"))
				)
				.andExpect(status().isUnauthorized())
				.andExpect(content().string(""));
		}

		@Test
		@Transactional
		void jwtLoginGivesWorkingTokens() throws Exception {
			MvcResult mvcResult = mvc.perform(
					post("/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.createObjectNode()
							.put("username", USERNAME)
							.put("password", PASSWORD)
							.toString()
						)
				)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andReturn();
			String content = mvcResult.getResponse().getContentAsString();
			String accessToken = JsonPath.read(content, "$.accessToken");
			String refreshToken = JsonPath.read(content, "$.refreshToken");
			int userId = JsonPath.read(content, "$.userId");

			mvc.perform(
					get("/home/")
						.header(AUTHORIZATION, jwt(accessToken))
				)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.cookies").value(true));

			mvc.perform(
					post("/auth/get_access")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.createObjectNode()
							.put("refreshToken", refreshToken)
							.put("userId", userId)
							.toString()
						)
				)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.accessToken").exists())
				.andExpect(jsonPath("$.refreshToken").exists());
		}
	}
}
