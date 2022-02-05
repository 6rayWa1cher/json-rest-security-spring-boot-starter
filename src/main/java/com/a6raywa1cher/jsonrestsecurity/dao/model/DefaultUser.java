package com.a6raywa1cher.jsonrestsecurity.dao.model;

/**
 * The default implementation of {@link IUser} and {@link AbstractUser}.
 * <br/>
 * It's not intended to use this class on the production.
 */
public final class DefaultUser extends AbstractUser implements Cloneable {
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
