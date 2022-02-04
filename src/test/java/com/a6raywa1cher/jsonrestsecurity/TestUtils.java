package com.a6raywa1cher.jsonrestsecurity;

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
}
