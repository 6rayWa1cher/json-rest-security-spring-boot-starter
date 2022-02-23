package com.a6raywa1cher.jsonrestsecurity.rest.res;

import java.util.Objects;

public class UserInfo {
	private Long id;

	private String username;

	public UserInfo(Long id, String username) {
		this.id = id;
		this.username = username;
	}

	@Override
	public String toString() {
		return "UserInfo{" +
			"id=" + id +
			", username='" + username + '\'' +
			'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		UserInfo userInfo = (UserInfo) o;
		return Objects.equals(id, userInfo.id) && Objects.equals(username, userInfo.username);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, username);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
