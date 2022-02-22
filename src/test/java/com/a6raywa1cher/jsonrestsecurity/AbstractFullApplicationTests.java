package com.a6raywa1cher.jsonrestsecurity;

import com.a6raywa1cher.jsonrestsecurity.dao.model.AbstractUser;
import com.a6raywa1cher.jsonrestsecurity.dao.repo.IUserRepository;
import com.a6raywa1cher.jsonrestsecurity.faillimiter.FailLimiterService;
import com.a6raywa1cher.jsonrestsecurity.jwt.JwtRefreshPair;
import com.a6raywa1cher.jsonrestsecurity.web.JsonRestWebSecurityConfigurer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.a6raywa1cher.jsonrestsecurity.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
public abstract class AbstractFullApplicationTests<T extends AbstractUser> {
	protected final String USERNAME = "abcdef";
	protected final String PASSWORD = "qwerty";
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
	@Autowired
	protected FailLimiterService service;

	@BeforeEach
	void resetFailLimiter() {
		service.resetStats();
	}

	@Test
	void contextLoads() {

	}

	@Test
	void beansArePresent() {
		assertThat(ctx.containsBean("authController")).isTrue();
		assertThat(ctx.getBeanNamesForType(JsonRestWebSecurityConfigurer.class)).hasSize(1);
		assertThat(ctx.containsBean("failLimiterFilter")).isTrue();
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
		new WithUser(USERNAME, PASSWORD) {
			@Override
			void run() throws Exception {
				securePerform(get("/home/"))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.cookies").value(true));

				securePerform(get("/auth/check"))
					.andExpect(status().isOk());

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
		};
	}


	@Test
	@Transactional
	void jwtLoginBlocksInvalidUser() {
		createNewUser(USERNAME, PASSWORD);
		assertThrows(AssertionError.class, () -> getJwtTokens(USERNAME, PASSWORD + "1"), "Status");
		assertThrows(AssertionError.class, () -> getJwtTokens(USERNAME + "1", PASSWORD), "Status");
	}

	@Test
	@Transactional
	void jwtRefreshTokenCanBeExchangedToAValidAccessToken() throws Exception {
		new WithUser(USERNAME, PASSWORD) {
			@Override
			void run() throws Exception {
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
				this.accessToken = JsonPath.read(content, "$.accessToken");

				securePerform(get("/home/"))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.cookies").value(true));
			}
		};
	}

	@Test
	void criticalActionLimiterTest() throws Exception {
		for (int i = 0; i < 5; i++) {
			mvc.perform(get("/home").with(remoteHost("127.5.0.1")))
				.andExpect(status().isUnauthorized());
		}
		mvc.perform(get("/home").with(remoteHost("127.5.0.1")))
			.andExpect(status().isTooManyRequests());
		mvc.perform(get("/home").with(remoteHost("127.5.0.2")))
			.andExpect(status().isUnauthorized());
	}

	@Test
	@Transactional
	void invalidateTokenTest() throws Exception {
		new WithUser(USERNAME, PASSWORD) {
			@Override
			void run() throws Exception {
				securePerform(get("/auth/check"))
					.andExpect(status().isOk());

				JwtRefreshPair anotherPair = getJwtTokens(USERNAME, PASSWORD);
				String accessToken2 = anotherPair.getAccessToken();

				mvc.perform(get("/auth/check").header(AUTHORIZATION, jwt(accessToken2)))
					.andExpect(status().isOk());

				securePerform(
					delete("/auth/invalidate")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.createObjectNode()
							.put("refreshToken", refreshToken)
							.put("userId", userId)
							.toString()
						)
				)
					.andExpect(status().isOk());

				securePerform(get("/auth/check"))
					.andExpect(status().isUnauthorized());  // access token is invalid

				mvc.perform(
						post("/auth/get_access")
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.createObjectNode()
								.put("refreshToken", refreshToken)
								.put("userId", userId)
								.toString()
							)
					)
					.andExpect(status().isNotFound()); // refresh token is invalid

				mvc.perform(get("/auth/check").header(AUTHORIZATION, jwt(accessToken2)))
					.andExpect(status().isOk()); // another token is still valid
			}
		};
	}

	@Test
	@Transactional
	void invalidateAllTokesTest() throws Exception {
		new WithUser(USERNAME, PASSWORD) {
			@Override
			void run() throws Exception {
				securePerform(get("/auth/check"))
					.andExpect(status().isOk());

				JwtRefreshPair anotherPair = getJwtTokens(USERNAME, PASSWORD);
				String accessToken2 = anotherPair.getAccessToken();
				String refreshToken2 = anotherPair.getRefreshToken();

				mvc.perform(get("/auth/check").header(AUTHORIZATION, jwt(accessToken2)))
					.andExpect(status().isOk());

				securePerform(
					delete("/auth/invalidate_all")
				)
					.andExpect(status().isOk());

				securePerform(get("/auth/check"))
					.andExpect(status().isUnauthorized());  // access token is invalid

				mvc.perform(
						post("/auth/get_access")
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.createObjectNode()
								.put("refreshToken", refreshToken)
								.put("userId", userId)
								.toString()
							)
					)
					.andExpect(status().isNotFound()); // refresh token is invalid

				mvc.perform(get("/auth/check").header(AUTHORIZATION, jwt(accessToken2)))
					.andExpect(status().isUnauthorized()); // another access token is invalid too

				mvc.perform(
						post("/auth/get_access")
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.createObjectNode()
								.put("refreshToken", refreshToken2)
								.put("userId", userId)
								.toString()
							)
					)
					.andExpect(status().isNotFound()); // another refresh token is invalid too
			}
		};
	}

	@Test
	@Transactional
	void lastVisitFilterTest() throws Exception {
		new WithUser(USERNAME, PASSWORD) {
			@Override
			void run() throws Exception {
				T user = userRepository.findByUsername(USERNAME).orElseThrow();
				LocalDateTime before = LocalDateTime.now().minusDays(15);
				user.setLastVisitAt(before);
				userRepository.save(user);

				securePerform(get("/auth/check"))
					.andExpect(status().isOk());

				T user2 = userRepository.findByUsername(USERNAME).orElseThrow();
				LocalDateTime after = user2.getLastVisitAt();
				assertThat(before).isBefore(after);
			}
		};
	}

	@Test
	@Transactional
	void firstUserTest() throws Exception {
		new WithUser("admin", "admin", false) {
			@Override
			void run() throws Exception {
				securePerform(get("/auth/check"))
					.andExpect(status().isOk());
			}
		};
	}

	abstract class WithUser {
		protected String accessToken;

		protected String refreshToken;

		protected long userId;

		public WithUser(String username, String password, boolean create) throws Exception {
			if (create) createNewUser(username, password);
			JwtRefreshPair jwtTokens = getJwtTokens(username, password);
			this.accessToken = jwtTokens.getAccessToken();
			this.refreshToken = jwtTokens.getRefreshToken();
			this.userId = jwtTokens.getUserId();
			this.run();
		}

		public WithUser(String username, String password) throws Exception {
			this(username, password, true);
		}

		abstract void run() throws Exception;

		ResultActions securePerform(MockHttpServletRequestBuilder builder) throws Exception {
			return mvc.perform(
				builder
					.header(AUTHORIZATION, jwt(accessToken))
			);
		}
	}
}
