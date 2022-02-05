package com.a6raywa1cher.jsonrestsecurity.jwt;

import com.a6raywa1cher.jsonrestsecurity.dao.DaoConfiguration;
import com.a6raywa1cher.jsonrestsecurity.dao.repo.RefreshTokenRepository;
import com.a6raywa1cher.jsonrestsecurity.jwt.service.BlockedRefreshTokensService;
import com.a6raywa1cher.jsonrestsecurity.jwt.service.JwtRefreshPairService;
import com.a6raywa1cher.jsonrestsecurity.jwt.service.JwtTokenService;
import com.a6raywa1cher.jsonrestsecurity.jwt.service.RefreshTokenService;
import com.a6raywa1cher.jsonrestsecurity.jwt.service.impl.BlockedRefreshTokensServiceImpl;
import com.a6raywa1cher.jsonrestsecurity.jwt.service.impl.JwtRefreshPairServiceImpl;
import com.a6raywa1cher.jsonrestsecurity.jwt.service.impl.JwtTokenServiceImpl;
import com.a6raywa1cher.jsonrestsecurity.jwt.service.impl.RefreshTokenServiceImpl;
import com.a6raywa1cher.jsonrestsecurity.utils.RandomUtils;
import com.a6raywa1cher.jsonrestsecurity.web.JsonRestSecurityConfigProperties;
import com.a6raywa1cher.jsonrestsecurity.web.JsonRestSecurityPropertiesConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Objects;

import static com.a6raywa1cher.jsonrestsecurity.utils.LogUtils.log;

@Configuration
@EnableTransactionManagement
@Import({JsonRestSecurityPropertiesConfiguration.class, DaoConfiguration.class})
public class JwtAuthConfiguration {
	private final JsonRestSecurityConfigProperties.JwtConfigProperties properties;

	public JwtAuthConfiguration(JsonRestSecurityConfigProperties properties) {
		this.properties = properties.getJwt();
	}

	@Bean
	@ConditionalOnMissingBean(BlockedRefreshTokensService.class)
	public BlockedRefreshTokensService blockedRefreshTokensService() {
		return new BlockedRefreshTokensServiceImpl(properties.getRefreshDuration());
	}

	@Bean
	@ConditionalOnMissingBean(JwtTokenService.class)
	public JwtTokenService jwtTokenService() {
		String secret = properties.getSecret();
		if (Objects.equals(secret, "generate")) {
			log.warn("\n\n\nUsing auto-generated JWT secret will cause every token to become invalid after an application restart!\nSet web-security.jwt.secret property (for example, visit this website: https://www.grc.com/passwords.htm )\n\n\n");
			secret = RandomUtils.randomString(128);
		}
		return new JwtTokenServiceImpl(
			properties.getIssuerName(),
			secret,
			properties.getAccessDuration()
		);
	}

	@Bean
	@DependsOn("refreshTokenRepository")
	@ConditionalOnMissingBean(RefreshTokenService.class)
	public RefreshTokenService refreshTokenService(
		RefreshTokenRepository repository, BlockedRefreshTokensService service
	) {
		return new RefreshTokenServiceImpl(
			repository,
			service,
			(long) properties.getMaxRefreshTokensPerUser(),
			properties.getRefreshDuration()
		);
	}

	@Bean
	@ConditionalOnMissingBean(JwtRefreshPairService.class)
	public JwtRefreshPairService jwtRefreshPairService(
		JwtTokenService jwtTokenService, RefreshTokenService refreshTokenService
	) {
		return new JwtRefreshPairServiceImpl(refreshTokenService, jwtTokenService);
	}
}
