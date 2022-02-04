package com.a6raywa1cher.jsonrestsecurity.dao.service;

import com.a6raywa1cher.jsonrestsecurity.dao.model.IUser;
import com.a6raywa1cher.jsonrestsecurity.dao.repo.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@ConditionalOnMissingBean(UserService.class)
@SuppressWarnings({"rawtypes", "unchecked"})
public class DefaultUserService implements UserService {
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DefaultUserService(IUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void updateLastVisitAt(IUser user) {
        user.setLastVisitAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public Optional<IUser> getById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<IUser> getByLogin(String login) {
        return userRepository.findByUsername(login);
    }

    @Override
    public Optional<IUser> getByLoginAndPassword(String username, String rawPassword) {
        Optional<IUser> optional = userRepository.findByUsername(username);
        return optional.filter(u -> passwordEncoder.matches(rawPassword, u.getPassword()));
    }
}
