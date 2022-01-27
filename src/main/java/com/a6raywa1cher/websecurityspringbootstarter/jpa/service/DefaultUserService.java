package com.a6raywa1cher.websecurityspringbootstarter.jpa.service;

import com.a6raywa1cher.websecurityspringbootstarter.jpa.model.AbstractUser;
import com.a6raywa1cher.websecurityspringbootstarter.jpa.repo.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@ConditionalOnMissingBean(UserService.class)
public class DefaultUserService implements UserService {
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DefaultUserService(IUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void updateLastVisitAt(AbstractUser user) {
        user.setLastVisitAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public Optional<AbstractUser> getById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<AbstractUser> getByLogin(String login) {
        return userRepository.findByUsername(login);
    }

    @Override
    public Optional<AbstractUser> getByLoginAndPassword(String username, String rawPassword) {
        Optional<AbstractUser> optional = userRepository.findByUsername(username);
        return optional.filter(u -> passwordEncoder.matches(rawPassword, u.getPassword()));
    }
}
