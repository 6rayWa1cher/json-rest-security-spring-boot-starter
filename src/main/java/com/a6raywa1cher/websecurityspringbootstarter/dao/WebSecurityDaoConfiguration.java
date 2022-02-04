package com.a6raywa1cher.websecurityspringbootstarter.dao;

import com.a6raywa1cher.websecurityspringbootstarter.dao.repo.DefaultRefreshTokenRepository;
import com.a6raywa1cher.websecurityspringbootstarter.dao.repo.DefaultUserRepository;
import com.a6raywa1cher.websecurityspringbootstarter.dao.repo.IUserRepository;
import com.a6raywa1cher.websecurityspringbootstarter.dao.repo.RefreshTokenRepository;
import com.a6raywa1cher.websecurityspringbootstarter.dao.service.DefaultUserService;
import com.a6raywa1cher.websecurityspringbootstarter.dao.service.UserService;
import com.a6raywa1cher.websecurityspringbootstarter.web.PasswordEncoderConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.a6raywa1cher.websecurityspringbootstarter.utils.LogUtils.log;

@Configuration
@AutoConfigureAfter(JpaRepositoriesAutoConfiguration.class)
@Import(PasswordEncoderConfiguration.class)
public class WebSecurityDaoConfiguration {
	private final ApplicationContext applicationContext;

	public WebSecurityDaoConfiguration(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@Bean
	@ConditionalOnMissingBean(type = "IUserRepository")
	public IUserRepository defaultUserRepository() {
		if (applicationContext.getBeansOfType(IUserRepository.class).isEmpty()) {
			log.warn("\n\n\nUsing default UserRepository implementation will cause wipe of users after an application restart!\nCreate your own implementation of IUserRepository or create subinterface\n\n\n");
			return new DefaultUserRepository();
		} else {
			return null;
		}
	}

	@Bean
	@ConditionalOnMissingBean(type = "RefreshTokenRepository")
	public RefreshTokenRepository refreshTokenRepository(IUserRepository userRepository) {
		return new DefaultRefreshTokenRepository(userRepository);
	}

	@Bean
	@ConditionalOnMissingBean(type = "UserService")
	public UserService userService(IUserRepository userRepository, PasswordEncoder passwordEncoder) {
		return new DefaultUserService(userRepository, passwordEncoder);
	}
}
