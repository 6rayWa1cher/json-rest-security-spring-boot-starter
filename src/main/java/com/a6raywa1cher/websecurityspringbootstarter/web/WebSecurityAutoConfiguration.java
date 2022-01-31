package com.a6raywa1cher.websecurityspringbootstarter.web;

import com.a6raywa1cher.websecurityspringbootstarter.component.authority.GrantedAuthorityService;
import com.a6raywa1cher.websecurityspringbootstarter.component.authority.GrantedAuthorityServiceImpl;
import com.a6raywa1cher.websecurityspringbootstarter.component.checker.DefaultUserEnabledChecker;
import com.a6raywa1cher.websecurityspringbootstarter.component.checker.UserEnabledChecker;
import com.a6raywa1cher.websecurityspringbootstarter.component.lastvisit.LastVisitFilter;
import com.a6raywa1cher.websecurityspringbootstarter.component.resolver.AuthenticationResolver;
import com.a6raywa1cher.websecurityspringbootstarter.component.resolver.AuthenticationResolverImpl;
import com.a6raywa1cher.websecurityspringbootstarter.dao.WebSecurityDaoConfiguration;
import com.a6raywa1cher.websecurityspringbootstarter.dao.service.UserService;
import com.a6raywa1cher.websecurityspringbootstarter.jwt.JwtAuthenticationFilter;
import com.a6raywa1cher.websecurityspringbootstarter.jwt.service.BlockedRefreshTokensService;
import com.a6raywa1cher.websecurityspringbootstarter.jwt.service.JwtTokenService;
import com.a6raywa1cher.websecurityspringbootstarter.providers.JwtAuthenticationProvider;
import com.a6raywa1cher.websecurityspringbootstarter.providers.UsernamePasswordAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableConfigurationProperties(WebSecurityConfigProperties.class)
@AutoConfigureAfter({PasswordEncoderAutoConfiguration.class, WebSecurityDaoConfiguration.class, AuthenticationEntryPointConfiguration.class})
public class WebSecurityAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(CorsConfigurationSource.class)
    public CorsConfigurationSource corsConfigurationSource(WebSecurityConfigProperties webSecurityConfigProperties) {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(webSecurityConfigProperties.getCorsAllowedOrigins()));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PATCH", "PUT", "HEAD", "OPTIONS"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    @ConditionalOnBean(UserService.class)
    @ConditionalOnMissingBean(AuthenticationResolver.class)
    public AuthenticationResolver authenticationResolver(UserService userService) {
        return new AuthenticationResolverImpl(userService);
    }

    @Bean
    @ConditionalOnMissingBean(UserEnabledChecker.class)
    public UserEnabledChecker userEnabledChecker() {
        return new DefaultUserEnabledChecker();
    }

    @Bean
    @ConditionalOnMissingBean(GrantedAuthorityService.class)
    public GrantedAuthorityService grantedAuthorityService(UserEnabledChecker userEnabledChecker) {
        return new GrantedAuthorityServiceImpl(userEnabledChecker);
    }

    @Configuration
    @Order(-1)
    @ConditionalOnProperty(prefix = "web-security", value = "enable-default-web-config")
    @EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
    public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {
        private final UserService userService;

        private final WebSecurityConfigProperties webSecurityConfigProperties;

        private final JwtTokenService jwtTokenService;

        private final AuthenticationResolver authenticationResolver;

        private final PasswordEncoder passwordEncoder;

        private final BlockedRefreshTokensService blockedRefreshTokensService;

        private final GrantedAuthorityService grantedAuthorityService;

        private final AuthenticationEntryPoint authenticationEntryPoint;

        private final CorsConfigurationSource corsConfigurationSource;

        @Autowired
        public WebSecurityConfigurer(UserService userService,
                                     WebSecurityConfigProperties webSecurityConfigProperties,
                                     JwtTokenService jwtTokenService,
                                     AuthenticationResolver authenticationResolver,
                                     PasswordEncoder passwordEncoder,
                                     BlockedRefreshTokensService blockedRefreshTokensService,
                                     GrantedAuthorityService grantedAuthorityService,
                                     AuthenticationEntryPoint authenticationEntryPoint,
                                     CorsConfigurationSource corsConfigurationSource) {
            this.userService = userService;
            this.webSecurityConfigProperties = webSecurityConfigProperties;
            this.jwtTokenService = jwtTokenService;
            this.authenticationResolver = authenticationResolver;
            this.passwordEncoder = passwordEncoder;
            this.blockedRefreshTokensService = blockedRefreshTokensService;
            this.grantedAuthorityService = grantedAuthorityService;
            this.authenticationEntryPoint = authenticationEntryPoint;
            this.corsConfigurationSource = corsConfigurationSource;
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) {
            auth
                    .authenticationProvider(new JwtAuthenticationProvider(userService, blockedRefreshTokensService, grantedAuthorityService))
                    .authenticationProvider(new UsernamePasswordAuthenticationProvider(userService, passwordEncoder, grantedAuthorityService));
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable();
            http.sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            http.authorizeRequests()
                    .antMatchers("/error", "/login").permitAll()
                    .antMatchers(HttpMethod.OPTIONS).permitAll()
                    .antMatchers("/").permitAll()
                    .antMatchers("/v3/api-docs/**", "/webjars/**", "/swagger-resources", "/swagger-resources/**",
                            "/swagger-ui.html", "/swagger-ui/**").permitAll()
                    .antMatchers("/csrf").permitAll()
                    .antMatchers("/auth/login").permitAll()
                    .antMatchers("/auth/get_access").permitAll()
                    .antMatchers("/auth/**").authenticated()
                    .antMatchers("/favicon.ico").permitAll()
                    .anyRequest().access("hasRole('USER') && hasAuthority('ENABLED')");
            http.cors()
                    .configurationSource(corsConfigurationSource);
            http.httpBasic()
                    .authenticationEntryPoint(authenticationEntryPoint);
            http.formLogin();
            http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenService, authenticationManagerBean()), UsernamePasswordAuthenticationFilter.class);
            http.addFilterAfter(new LastVisitFilter(userService, authenticationResolver), SecurityContextHolderAwareRequestFilter.class);
        }
    }

}
