package com.a6raywa1cher.jsonrestsecurity;

import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class TestUtils {
	public static final String AUTHORIZATION = "Authorization";

	public static String basic(String login, String password) {
		String encoded = Base64.getEncoder().encodeToString((login + ":" + password).getBytes(StandardCharsets.UTF_8));
		return "Basic " + encoded;
	}

	public static String jwt(String accessToken) {
		return "Bearer " + accessToken;
	}

	// https://stackoverflow.com/a/53701614
	public static RequestPostProcessor remoteHost(final String remoteHost) {
		return request -> {
			request.setRemoteAddr(remoteHost);
			return request;
		};
	}
}
