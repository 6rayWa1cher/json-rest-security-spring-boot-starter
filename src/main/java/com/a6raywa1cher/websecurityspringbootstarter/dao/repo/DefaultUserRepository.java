package com.a6raywa1cher.websecurityspringbootstarter.dao.repo;

import com.a6raywa1cher.websecurityspringbootstarter.dao.model.DefaultUser;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class DefaultUserRepository implements IUserRepository<DefaultUser> {
	private final Map<Long, DefaultUser> db = new HashMap<>();
	private final AtomicLong lastId = new AtomicLong(0);

	private <S extends DefaultUser> S clone(S defaultUser) {
		try {
			//noinspection unchecked
			return (S) defaultUser.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}


	@Override
	public Optional<DefaultUser> findByUsername(String username) {
		return db.values().stream()
			.filter(u -> Objects.equals(u.getUsername(), username))
			.findFirst()
			.map(this::clone);
	}

	@Override
	public <S extends DefaultUser> S save(S user) {
		if (user.getId() == null) {
			user.setId(lastId.incrementAndGet());
		}
		db.put(user.getId(), user);
		return clone(user);
	}

	@Override
	public Optional<DefaultUser> findById(Long id) {
		return Optional.ofNullable(db.get(id)).map(this::clone);
	}
}
