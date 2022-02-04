package com.a6raywa1cher.websecurityspringbootstarter.jwt;

import com.a6raywa1cher.websecurityspringbootstarter.dao.WebSecurityDaoConfiguration;
import com.a6raywa1cher.websecurityspringbootstarter.dao.repo.RefreshTokenRepository;
import com.a6raywa1cher.websecurityspringbootstarter.jwt.service.BlockedRefreshTokensService;
import com.a6raywa1cher.websecurityspringbootstarter.jwt.service.JwtRefreshPairService;
import com.a6raywa1cher.websecurityspringbootstarter.jwt.service.JwtTokenService;
import com.a6raywa1cher.websecurityspringbootstarter.jwt.service.RefreshTokenService;
import com.a6raywa1cher.websecurityspringbootstarter.jwt.service.impl.BlockedRefreshTokensServiceImpl;
import com.a6raywa1cher.websecurityspringbootstarter.jwt.service.impl.JwtRefreshPairServiceImpl;
import com.a6raywa1cher.websecurityspringbootstarter.jwt.service.impl.JwtTokenServiceImpl;
import com.a6raywa1cher.websecurityspringbootstarter.jwt.service.impl.RefreshTokenServiceImpl;
import com.a6raywa1cher.websecurityspringbootstarter.utils.RandomUtils;
import com.a6raywa1cher.websecurityspringbootstarter.web.WebSecurityConfigProperties;
import com.a6raywa1cher.websecurityspringbootstarter.web.WebSecurityPropertiesConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Objects;

import static com.a6raywa1cher.websecurityspringbootstarter.utils.LogUtils.log;

@Configuration
@EnableTransactionManagement
@Import({WebSecurityPropertiesConfiguration.class, WebSecurityDaoConfiguration.class})
public class JwtAuthConfiguration {
	private final WebSecurityConfigProperties.JwtConfigProperties properties;

	public JwtAuthConfiguration(WebSecurityConfigProperties properties) {
		this.properties = properties.getJwt();
	}

	@Bean
	@ConditionalOnMissingBean(type = "BlockedRefreshTokensService")
	public BlockedRefreshTokensService blockedRefreshTokensService() {
		return new BlockedRefreshTokensServiceImpl(properties.getRefreshDuration());
    }

	@Bean
	@ConditionalOnMissingBean(type = "JwtTokenService")
    public JwtTokenService jwtTokenService() {
		String secret = properties.getSecret();
		if (Objects.equals(secret, "generated")) {
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
	@ConditionalOnMissingBean(type = "RefreshTokenService")
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
	@ConditionalOnMissingBean(type = "JwtRefreshPairService")
    public JwtRefreshPairService jwtRefreshPairService(
            JwtTokenService jwtTokenService, RefreshTokenService refreshTokenService
    ) {
        return new JwtRefreshPairServiceImpl(refreshTokenService, jwtTokenService);
    }
}
