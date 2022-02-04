package com.a6raywa1cher.websecurityspringbootstarter.dao.repo;

import com.a6raywa1cher.websecurityspringbootstarter.dao.model.IUser;

import java.util.Optional;

public interface IUserRepository<T extends IUser> {
	Optional<T> findByUsername(String username);

	<S extends T> S save(S s);

	Optional<T> findById(Long id);
}
