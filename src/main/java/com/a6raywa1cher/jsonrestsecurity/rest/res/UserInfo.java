package com.a6raywa1cher.jsonrestsecurity.rest.res;

import java.util.Objects;

public class UserInfo {
	private Long id;

	private String username;

	private String userRole;

	public UserInfo(Long id, String username, String userRole) {
		this.id = id;
		this.username = username;
		this.userRole = userRole;
	}

	@Override
	public String toString() {
		return "UserInfo{" +
			"id=" + id +
			", username='" + username + '\'' +
			", userRole='" + userRole + '\'' +
			'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		UserInfo userInfo = (UserInfo) o;
		return Objects.equals(id, userInfo.id) &&
			Objects.equals(username, userInfo.username) &&
			Objects.equals(userRole, userInfo.userRole);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, username, userRole);
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

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
}
