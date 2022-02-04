package com.a6raywa1cher.websecurityspringbootstarter.dao.model;

public class DefaultUser extends AbstractUser implements Cloneable {
	@Override
	public DefaultUser clone() throws CloneNotSupportedException {
		DefaultUser defaultUser = (DefaultUser) super.clone();

		defaultUser.setRefreshTokens(defaultUser.getRefreshTokens().stream().map(refreshToken -> {
			try {
				return refreshToken.clone();
			} catch (CloneNotSupportedException e) {
				throw new RuntimeException(e);
			}
		}).toList());

		return defaultUser;
	}
}
