package com.a6raywa1cher.jsonrestsecurity.jwt;

import com.a6raywa1cher.jsonrestsecurity.dao.DaoConfiguration;
import com.a6raywa1cher.jsonrestsecurity.dao.repo.RefreshTokenRepository;
import com.a6raywa1cher.jsonrestsecurity.jwt.service.BlockedRefreshTokensService;
import com.a6raywa1cher.jsonrestsecurity.jwt.service.JwtRefreshPairService;
import com.a6raywa1cher.jsonrestsecurity.jwt.service.JwtTokenService;
import com.a6raywa1cher.jsonrestsecurity.jwt.service.RefreshTokenService;
import com.a6raywa1cher.jsonrestsecurity.jwt.service.impl.JwtRefreshPairServiceImpl;
import com.a6raywa1cher.jsonrestsecurity.jwt.service.impl.JwtTokenServiceImpl;
import com.a6raywa1cher.jsonrestsecurity.jwt.service.impl.LoadingCacheBlockedRefreshTokensService;
import com.a6raywa1cher.jsonrestsecurity.jwt.service.impl.RefreshTokenServiceImpl;
import com.a6raywa1cher.jsonrestsecurity.utils.StringUtils;
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

/**
 * Configures all beans from {@link com.a6raywa1cher.jsonrestsecurity.jwt} package.
 */
@Configuration
@EnableTransactionManagement
@Import({JsonRestSecurityPropertiesConfiguration.class, DaoConfiguration.class})
@SuppressWarnings("SpringFacetCodeInspection")
public class JwtAuthConfiguration {
	private final JsonRestSecurityConfigProperties.JwtConfigProperties properties;

	public JwtAuthConfiguration(JsonRestSecurityConfigProperties properties) {
		this.properties = properties.getJwt();
	}

	/**
	 * Creates the {@link BlockedRefreshTokensService} bean with {@link LoadingCacheBlockedRefreshTokensService} implementation.
	 * <br/>
	 * Access token life duration is being taken from {@code json-rest-security.jwt.access-duration} property (default 5 minutes).
	 *
	 * @return BlockedRefreshTokensService bean
	 */
	@Bean
	@ConditionalOnMissingBean(BlockedRefreshTokensService.class)
	public BlockedRefreshTokensService blockedRefreshTokensService() {
		return new LoadingCacheBlockedRefreshTokensService(properties.getAccessDuration());
	}

	/**
	 * Creates the {@link JwtTokenService} bean with {@link JwtTokenServiceImpl} implementation.
	 * <br/>
	 * JWT secret is being taken from {@code json-rest-security.jwt.secret} property. If the property value
	 * is {@code "generate"}, then the JWT secret will be generated.
	 *
	 * @return JwtTokenService bean
	 */
	@Bean
	@ConditionalOnMissingBean(JwtTokenService.class)
	public JwtTokenService jwtTokenService() {
		String secret = properties.getSecret();
		if (Objects.equals(secret, "generate")) {
			log.warn("\n\n\nUsing auto-generated JWT secret will cause every token to become invalid after an application restart!\nSet json-rest-security.jwt.secret property (for example, visit this website: https://www.grc.com/passwords.htm )\n\n\n");
			secret = StringUtils.randomString(128);
		}
		return new JwtTokenServiceImpl(
			properties.getIssuerName(),
			secret,
			properties.getAccessDuration()
		);
	}

	/**
	 * Creates the {@link RefreshTokenService} bean with {@link RefreshTokenServiceImpl} implementation.
	 * <br/>
	 * Max refresh token count per user is being taken from {@code json-rest-security.jwt.max-refresh-tokens-per-user}
	 * property (default is 10).
	 * <br/>
	 * Refresh token life duration is being taken from {@code json-rest-security.jwt.refresh-duration} property
	 * (default 14 days).
	 *
	 * @param repository RefreshTokenRepository bean
	 * @param service    BlockedRefreshTokensService bean
	 * @return RefreshTokenService bean
	 */
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

	/**
	 * Creates the {@link JwtRefreshPairService} bean with {@link JwtRefreshPairServiceImpl} implementation.
	 *
	 * @param jwtTokenService     JwtTokenService bean
	 * @param refreshTokenService RefreshTokenService bean
	 * @return JwtRefreshPairService bean
	 */
	@Bean
	@ConditionalOnMissingBean(JwtRefreshPairService.class)
	public JwtRefreshPairService jwtRefreshPairService(
		JwtTokenService jwtTokenService, RefreshTokenService refreshTokenService
	) {
		return new JwtRefreshPairServiceImpl(refreshTokenService, jwtTokenService);
	}
}
