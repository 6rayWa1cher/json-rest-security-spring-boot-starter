package com.a6raywa1cher.jsonrestsecurity.dao;

import com.a6raywa1cher.jsonrestsecurity.dao.repo.DefaultRefreshTokenRepository;
import com.a6raywa1cher.jsonrestsecurity.dao.repo.DefaultUserRepository;
import com.a6raywa1cher.jsonrestsecurity.dao.repo.IUserRepository;
import com.a6raywa1cher.jsonrestsecurity.dao.repo.RefreshTokenRepository;
import com.a6raywa1cher.jsonrestsecurity.dao.service.DefaultUserService;
import com.a6raywa1cher.jsonrestsecurity.dao.service.UserService;
import com.a6raywa1cher.jsonrestsecurity.web.PasswordEncoderConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.a6raywa1cher.jsonrestsecurity.utils.LogUtils.log;

/**
 * Configures all beans from {@link com.a6raywa1cher.jsonrestsecurity.dao} package.
 */
@Configuration
@Import(PasswordEncoderConfiguration.class)
public class DaoConfiguration {
	private final ApplicationContext applicationContext;

	public DaoConfiguration(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@Bean
	@ConditionalOnMissingBean(IUserRepository.class)
	public IUserRepository defaultUserRepository() {
		if (applicationContext.getBeansOfType(IUserRepository.class).isEmpty()) {
			log.warn("\n\n\nUsing default UserRepository implementation will cause wipe of users after an application restart!\nCreate your own implementation of IUserRepository or create subinterface\n\n\n");
			return new DefaultUserRepository();
		} else {
			return null;
		}
	}

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
