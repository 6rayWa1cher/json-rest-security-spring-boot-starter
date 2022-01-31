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
import com.a6raywa1cher.websecurityspringbootstarter.web.WebSecurityConfigProperties;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@AutoConfigureBefore({TransactionAutoConfiguration.class})
@AutoConfigureAfter({WebSecurityDaoConfiguration.class})
@EnableTransactionManagement
@EnableConfigurationProperties({
        WebSecurityConfigProperties.class
})
public class JwtAuthAutoConfiguration {
    private final WebSecurityConfigProperties.JwtConfigProperties properties;

    public JwtAuthAutoConfiguration(WebSecurityConfigProperties properties) {
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
        return new JwtTokenServiceImpl(
                properties.getIssuerName(),
                properties.getSecret(),
                properties.getAccessDuration()
        );
    }

    @Bean
    @ConditionalOnBean(RefreshTokenRepository.class)
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
