package com.a6raywa1cher.jsonrestsecurity.dao.repo;

import com.a6raywa1cher.jsonrestsecurity.dao.model.DefaultUser;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * The default implementation of {@link IUserRepository}.
 * <br/>
 * Stores users in HashMap {@link #db}. Works only with {@link DefaultUser}.
 *
 * @see com.a6raywa1cher.jsonrestsecurity.dao.DaoConfiguration
 */
public class DefaultUserRepository implements IUserRepository<DefaultUser> {
	private final Map<Long, DefaultUser> db = new HashMap<>();
	private final AtomicLong lastId = new AtomicLong(0);

	@SuppressWarnings("unchecked")
	private <S extends DefaultUser> S clone(S defaultUser) {
		try {
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
	public <S extends DefaultUser> Iterable<S> saveAll(Iterable<S> entities) {
		return StreamSupport.stream(entities.spliterator(), false)
			.peek(e -> db.put(e.getId(), e))
			.map(this::clone)
			.collect(Collectors.toList());
	}

	@Override
	public Optional<DefaultUser> findById(Long id) {
		return Optional.ofNullable(db.get(id)).map(this::clone);
	}

	@Override
	public boolean existsById(Long aLong) {
		return db.containsKey(aLong);
	}

	@Override
	public Iterable<DefaultUser> findAll() {
		return db.values().stream().map(this::clone).collect(Collectors.toList());
	}

	@Override
	public Iterable<DefaultUser> findAllById(Iterable<Long> longs) {
		return StreamSupport.stream(longs.spliterator(), false).map(db::get).collect(Collectors.toList());
	}

	@Override
	public long count() {
		return db.size();
	}

	@Override
	public void deleteById(Long aLong) {
		db.remove(aLong);
	}

	@Override
	public void delete(DefaultUser entity) {
		db.remove(entity.getId());
	}

	@Override
	public void deleteAllById(Iterable<? extends Long> longs) {
		longs.forEach(db::remove);
	}

	@Override
	public void deleteAll(Iterable<? extends DefaultUser> entities) {
		StreamSupport.stream(entities.spliterator(), false).map(DefaultUser::getId).forEach(db::remove);
	}

	@Override
	public void deleteAll() {
		db.clear();
	}
}
