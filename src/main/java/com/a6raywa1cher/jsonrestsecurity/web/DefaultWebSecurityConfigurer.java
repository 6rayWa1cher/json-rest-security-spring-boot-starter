package com.a6raywa1cher.jsonrestsecurity.web;

import com.a6raywa1cher.jsonrestsecurity.component.SecurityComponentsConfiguration;
import com.a6raywa1cher.jsonrestsecurity.component.authority.GrantedAuthorityService;
import com.a6raywa1cher.jsonrestsecurity.component.resolver.AuthenticationResolver;
import com.a6raywa1cher.jsonrestsecurity.dao.DaoConfiguration;
import com.a6raywa1cher.jsonrestsecurity.dao.service.UserService;
import com.a6raywa1cher.jsonrestsecurity.jwt.JwtAuthConfiguration;
import com.a6raywa1cher.jsonrestsecurity.jwt.JwtAuthenticationFilter;
import com.a6raywa1cher.jsonrestsecurity.jwt.service.BlockedRefreshTokensService;
import com.a6raywa1cher.jsonrestsecurity.jwt.service.JwtTokenService;
import com.a6raywa1cher.jsonrestsecurity.providers.JwtAuthenticationProvider;
import com.a6raywa1cher.jsonrestsecurity.providers.UsernamePasswordAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
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
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@Order(-1)
@ConditionalOnProperty(prefix = "web-security", value = "enable-default-web-config", havingValue = "true",
	matchIfMissing = true)
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Import({
	PasswordEncoderConfiguration.class,
	DaoConfiguration.class,
	JwtAuthConfiguration.class,
	SecurityComponentsConfiguration.class,
	JsonRestSecurityPropertiesConfiguration.class
})
public class DefaultWebSecurityConfigurer extends WebSecurityConfigurerAdapter {
	private final UserService userService;

	private final JwtTokenService jwtTokenService;

	private final AuthenticationResolver authenticationResolver;

	private final PasswordEncoder passwordEncoder;

	private final BlockedRefreshTokensService blockedRefreshTokensService;

	private final GrantedAuthorityService grantedAuthorityService;

	private final AuthenticationEntryPoint authenticationEntryPoint;

	private final CorsConfigurationSource corsConfigurationSource;

	@Autowired
	public DefaultWebSecurityConfigurer(UserService userService,
										JwtTokenService jwtTokenService,
										AuthenticationResolver authenticationResolver,
										PasswordEncoder passwordEncoder,
										BlockedRefreshTokensService blockedRefreshTokensService,
										GrantedAuthorityService grantedAuthorityService,
										AuthenticationEntryPoint authenticationEntryPoint,
										CorsConfigurationSource corsConfigurationSource) {
		this.userService = userService;
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