package com.a6raywa1cher.jsonrestsecurity.utils;

import java.security.SecureRandom;

// https://stackoverflow.com/a/157202
public class StringUtils {
	private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	private static final SecureRandom rnd = new SecureRandom();

	public static String randomString(int len) {
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++)
			sb.append(AB.charAt(rnd.nextInt(AB.length())));
		return sb.toString();
	}

	public static String erasePassword(String password) {
		return password == null || "".equals(password) ? "null" : "[PASSWORD]";
	}
}
