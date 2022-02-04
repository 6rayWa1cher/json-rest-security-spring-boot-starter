package com.a6raywa1cher.websecurityspringbootstarter;

import com.a6raywa1cher.websecurityspringbootstarter.dao.model.AbstractUser;
import com.a6raywa1cher.websecurityspringbootstarter.dao.repo.IUserRepository;
import com.a6raywa1cher.websecurityspringbootstarter.jwt.JwtRefreshPair;
import com.a6raywa1cher.websecurityspringbootstarter.web.DefaultWebSecurityConfigurer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static com.a6raywa1cher.websecurityspringbootstarter.TestUtils.basic;
import static com.a6raywa1cher.websecurityspringbootstarter.TestUtils.jwt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
public abstract class AbstractFullApplicationTests<T extends AbstractUser> {
	final String USERNAME = "abcdef";
	final String PASSWORD = "qwerty";
	@Autowired
	protected ApplicationContext ctx;
	@Autowired
	protected MockMvc mvc;
	@Autowired
	protected IUserRepository<T> userRepository;
	@Autowired
	protected PasswordEncoder passwordEncoder;
	@Autowired
	protected ObjectMapper objectMapper;

	@Test
	void contextLoads() {

	}

	@Test
	void beansArePresent() {
		assertThat(ctx.containsBean("authController")).isTrue();
		assertThat(ctx.getBeanNamesForType(DefaultWebSecurityConfigurer.class)).hasSize(1);
		assertThat(ctx.containsBean("criticalActionLimiterFilter")).isTrue();
	}

	protected abstract void createNewUser(String username, String password);

	@Test
	@Transactional
	void basicLoginPassesValidUser() throws Exception {
		createNewUser(USERNAME, PASSWORD);
		mvc.perform(
				get("/home/")
					.header(TestUtils.AUTHORIZATION, basic(USERNAME, PASSWORD))
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.cookies").value(true));
	}

	@Test
	@Transactional
	void basicLoginBlocksInvalidUser() throws Exception {
		createNewUser(USERNAME, PASSWORD);
		mvc.perform(
				get("/home/")
					.header(TestUtils.AUTHORIZATION, basic(USERNAME, PASSWORD + "1"))
			)
			.andExpect(status().isUnauthorized())
			.andExpect(content().string(""));
	}

	protected JwtRefreshPair getJwtTokens(String username, String password) throws Exception {
		MvcResult mvcResult = mvc.perform(
				post("/auth/login")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.createObjectNode()
						.put("username", username)
						.put("password", password)
						.toString()
					)
			)
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andReturn();
		String content = mvcResult.getResponse().getContentAsString();
		return objectMapper.readValue(content, JwtRefreshPair.class);
	}

	@Test
	@Transactional
	void jwtLoginGivesWorkingTokens() throws Exception {
		createNewUser(USERNAME, PASSWORD);
		JwtRefreshPair jwtTokens = getJwtTokens(USERNAME, PASSWORD);
		String accessToken = jwtTokens.getAccessToken();
		String refreshToken = jwtTokens.getRefreshToken();
		long userId = jwtTokens.getUserId();

		mvc.perform(
				get("/home/")
					.header(TestUtils.AUTHORIZATION, jwt(accessToken))
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

	@Test
	@Transactional
	void jwtRefreshTokenCanBeExchangedToAValidAccessToken() throws Exception {
		createNewUser(USERNAME, PASSWORD);
		JwtRefreshPair jwtTokens = getJwtTokens(USERNAME, PASSWORD);
		String refreshToken = jwtTokens.getRefreshToken();
		long userId = jwtTokens.getUserId();

		MvcResult mvcResult = mvc.perform(
				post("/auth/get_access")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.createObjectNode()
						.put("refreshToken", refreshToken)
						.put("userId", userId)
						.toString()
					)
			)
			.andExpect(status().isOk())
			.andReturn();
		String content = mvcResult.getResponse().getContentAsString();
		String newAccessToken = JsonPath.read(content, "$.accessToken");

		mvc.perform(
				get("/home/")
					.header(TestUtils.AUTHORIZATION, jwt(newAccessToken))
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.cookies").value(true));
	}
}
