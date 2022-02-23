package com.a6raywa1cher.jsonrestsecurity.dao.service;

import com.a6raywa1cher.jsonrestsecurity.dao.model.IUser;

import java.util.Optional;

/**
 * Provides methods to find and update users
 *
 * @see AbstractUserService
 */
public interface IUserService<T extends IUser> {
	/**
	 * Updates last visit timestamp to the current time.
	 *
	 * @param user the input user
	 */
	void updateLastVisitAt(T user);

	/**
	 * Retracts a user by id
	 *
	 * @param id user id
	 * @return user or empty optional
	 */
	Optional<T> getById(Long id);

	/**
	 * Retracts a user by login
	 *
	 * @param login user login
	 * @return user or empty optional
	 */
	Optional<T> getByLogin(String login);

	/**
	 * Retracts a user by login and raw password
	 *
	 * @param login       user login
	 * @param rawPassword user raw password
	 * @return user or empty optional
	 */
	Optional<T> getByLoginAndPassword(String login, String rawPassword);

	/**
	 * Creates a new user and save it
	 *
	 * @param login       user login
	 * @param rawPassword user raw password
	 * @param role        user role
	 * @return new and saved user
	 */
	T create(String login, String rawPassword, String role);
}
