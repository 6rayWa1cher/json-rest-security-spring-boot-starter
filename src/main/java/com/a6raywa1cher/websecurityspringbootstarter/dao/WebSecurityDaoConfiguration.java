package com.a6raywa1cher.websecurityspringbootstarter.dao;

import com.a6raywa1cher.websecurityspringbootstarter.dao.repo.DefaultRefreshTokenRepository;
import com.a6raywa1cher.websecurityspringbootstarter.dao.repo.IUserRepository;
import com.a6raywa1cher.websecurityspringbootstarter.dao.repo.RefreshTokenRepository;
import com.a6raywa1cher.websecurityspringbootstarter.dao.service.DefaultUserService;
import com.a6raywa1cher.websecurityspringbootstarter.dao.service.UserService;
import com.a6raywa1cher.websecurityspringbootstarter.web.PasswordEncoderConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@AutoConfigureAfter({PasswordEncoderConfiguration.class, JpaRepositoriesAutoConfiguration.class})
//@ConditionalOnBean(IUserRepository.class)
@Import(PasswordEncoderConfiguration.class)
public class WebSecurityDaoConfiguration {
    @Bean
    @ConditionalOnMissingBean(RefreshTokenRepository.class)
    public RefreshTokenRepository refreshTokenRepository(IUserRepository userRepository) {
        return new DefaultRefreshTokenRepository(userRepository);
    }

    @Bean
    @ConditionalOnMissingBean(UserService.class)
    public UserService userService(IUserRepository userRepository, PasswordEncoder passwordEncoder) {
        return new DefaultUserService(userRepository, passwordEncoder);
    }
}
