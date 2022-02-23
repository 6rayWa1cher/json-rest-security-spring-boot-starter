package com.a6raywa1cher.jsonrestsecurity.web;

import com.a6raywa1cher.jsonrestsecurity.component.SecurityComponentsConfiguration;
import com.a6raywa1cher.jsonrestsecurity.component.authority.GrantedAuthorityService;
import com.a6raywa1cher.jsonrestsecurity.component.resolver.AuthenticationResolver;
import com.a6raywa1cher.jsonrestsecurity.dao.DaoConfiguration;
import com.a6raywa1cher.jsonrestsecurity.dao.service.IUserService;
import com.a6raywa1cher.jsonrestsecurity.faillimiter.FailLimiterConfiguration;
import com.a6raywa1cher.jsonrestsecurity.faillimiter.FailLimiterFilter;
import com.a6raywa1cher.jsonrestsecurity.jwt.JwtAuthConfiguration;
import com.a6raywa1cher.jsonrestsecurity.jwt.JwtAuthenticationFilter;
import com.a6raywa1cher.jsonrestsecurity.jwt.service.BlockedRefreshTokensService;
import com.a6raywa1cher.jsonrestsecurity.jwt.service.JwtTokenService;
import com.a6raywa1cher.jsonrestsecurity.providers.JwtAuthenticationProvider;
import com.a6raywa1cher.jsonrestsecurity.providers.UsernamePasswordAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
@ConditionalOnProperty(prefix = "json-rest-security", value = "enable-default-web-config", havingValue = "true",
	matchIfMissing = true)
@ConditionalOnMissingBean(type = "org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter")
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Import({
	PasswordEncoderConfiguration.class,
	DaoConfiguration.class,
	JwtAuthConfiguration.class,
	SecurityComponentsConfiguration.class,
	JsonRestSecurityPropertiesConfiguration.class,
	FailLimiterConfiguration.class
})
@SuppressWarnings("SpringFacetCodeInspection")
public class JsonRestWebSecurityConfigurer extends WebSecurityConfigurerAdapter {
	private IUserService<?> IUserService;

	private JwtTokenService jwtTokenService;

	private AuthenticationResolver authenticationResolver;

	private PasswordEncoder passwordEncoder;

	private BlockedRefreshTokensService blockedRefreshTokensService;

	private GrantedAuthorityService grantedAuthorityService;

	private AuthenticationEntryPoint authenticationEntryPoint;

	private CorsConfigurationSource corsConfigurationSource;

	private FailLimiterFilter failLimiterFilter;

	private boolean useAnyMatcher = true;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) {
		auth
			.authenticationProvider(getJwtAuthenticationProvider())
			.authenticationProvider(getUsernamePasswordAuthenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		configureJsonRest(http);
		configureAuthorizeRequests(http);
		configureHttpAuth(http);
	}

	private UsernamePasswordAuthenticationProvider getUsernamePasswordAuthenticationProvider() {
		return new UsernamePasswordAuthenticationProvider(IUserService, passwordEncoder, grantedAuthorityService);
	}

	protected JwtAuthenticationProvider getJwtAuthenticationProvider() {
		return new JwtAuthenticationProvider(IUserService, blockedRefreshTokensService, grantedAuthorityService);
	}

	protected void configureJsonRest(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.cors()
			.configurationSource(corsConfigurationSource);
	}

	protected void configureHttpAuth(HttpSecurity http) throws Exception {
		http.httpBasic()
			.authenticationEntryPoint(authenticationEntryPoint);
		http.formLogin();
		http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenService, authenticationManagerBean(), authenticationEntryPoint), UsernamePasswordAuthenticationFilter.class);
		http.addFilterAfter(new LastVisitFilter<>(IUserService, authenticationResolver), SecurityContextHolderAwareRequestFilter.class);
		if (failLimiterFilter != null) http.addFilterBefore(failLimiterFilter, JwtAuthenticationFilter.class);
	}

	protected void configureAuthorizeRequests(HttpSecurity http) throws Exception {
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
			.antMatchers("/favicon.ico").permitAll();
		if (useAnyMatcher) {
			http.authorizeRequests().anyRequest().access("hasRole('USER') && hasAuthority('ENABLED')");
		}
	}

	@SuppressWarnings("unused")
	public void setUseAnyMatcher(boolean useAnyMatcher) {
		this.useAnyMatcher = useAnyMatcher;
	}

	@Autowired
	public void setUserService(IUserService<?> IUserService) {
		this.IUserService = IUserService;
	}

	@Autowired
	public void setJwtTokenService(JwtTokenService jwtTokenService) {
		this.jwtTokenService = jwtTokenService;
	}

	@Autowired
	public void setAuthenticationResolver(AuthenticationResolver authenticationResolver) {
		this.authenticationResolver = authenticationResolver;
	}

	@Autowired
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Autowired
	public void setBlockedRefreshTokensService(BlockedRefreshTokensService blockedRefreshTokensService) {
		this.blockedRefreshTokensService = blockedRefreshTokensService;
	}

	@Autowired
	public void setGrantedAuthorityService(GrantedAuthorityService grantedAuthorityService) {
		this.grantedAuthorityService = grantedAuthorityService;
	}

	@Autowired
	public void setAuthenticationEntryPoint(AuthenticationEntryPoint authenticationEntryPoint) {
		this.authenticationEntryPoint = authenticationEntryPoint;
	}

	@Autowired
	public void setCorsConfigurationSource(CorsConfigurationSource corsConfigurationSource) {
		this.corsConfigurationSource = corsConfigurationSource;
	}

	@Autowired(required = false)
	public void setFailLimiterFilter(FailLimiterFilter failLimiterFilter) {
		this.failLimiterFilter = failLimiterFilter;
	}
}