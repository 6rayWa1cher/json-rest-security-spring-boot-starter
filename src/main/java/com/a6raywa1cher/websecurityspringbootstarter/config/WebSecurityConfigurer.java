package com.a6raywa1cher.websecurityspringbootstarter.config;

import com.a6raywa1cher.websecurityspringbootstarter.authentication.CustomAuthenticationEntryPoint;
import com.a6raywa1cher.websecurityspringbootstarter.component.authority.GrantedAuthorityService;
import com.a6raywa1cher.websecurityspringbootstarter.component.lastvisit.LastVisitFilter;
import com.a6raywa1cher.websecurityspringbootstarter.component.resolver.AuthenticationResolver;
import com.a6raywa1cher.websecurityspringbootstarter.jpa.service.UserService;
import com.a6raywa1cher.websecurityspringbootstarter.jwt.JwtAuthenticationFilter;
import com.a6raywa1cher.websecurityspringbootstarter.jwt.service.BlockedRefreshTokensService;
import com.a6raywa1cher.websecurityspringbootstarter.jwt.service.JwtTokenService;
import com.a6raywa1cher.websecurityspringbootstarter.providers.JwtAuthenticationProvider;
import com.a6raywa1cher.websecurityspringbootstarter.providers.UsernamePasswordAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@Order(-1)
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {
	private final UserService userService;

	private final WebSecurityConfigProperties webSecurityConfigProperties;

	private final JwtTokenService jwtTokenService;

	private final AuthenticationResolver authenticationResolver;

	private final PasswordEncoder passwordEncoder;

	private final BlockedRefreshTokensService blockedRefreshTokensService;

	private final GrantedAuthorityService grantedAuthorityService;

	private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

	private final CorsConfigurationSource corsConfigurationSource;

	@Autowired
	public WebSecurityConfigurer(UserService userService,
								 WebSecurityConfigProperties webSecurityConfigProperties,
								 JwtTokenService jwtTokenService,
								 AuthenticationResolver authenticationResolver,
								 PasswordEncoder passwordEncoder,
								 BlockedRefreshTokensService blockedRefreshTokensService,
								 GrantedAuthorityService grantedAuthorityService,
								 CustomAuthenticationEntryPoint customAuthenticationEntryPoint,
								 CorsConfigurationSource corsConfigurationSource) {
		this.userService = userService;
		this.webSecurityConfigProperties = webSecurityConfigProperties;
		this.jwtTokenService = jwtTokenService;
		this.authenticationResolver = authenticationResolver;
		this.passwordEncoder = passwordEncoder;
		this.blockedRefreshTokensService = blockedRefreshTokensService;
		this.grantedAuthorityService = grantedAuthorityService;
		this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
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
				.authenticationEntryPoint(customAuthenticationEntryPoint);
		http.formLogin();
		http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenService, authenticationManagerBean()), UsernamePasswordAuthenticationFilter.class);
		http.addFilterAfter(new LastVisitFilter(userService, authenticationResolver), SecurityContextHolderAwareRequestFilter.class);
//		http.addFilterBefore(new CriticalActionLimiterFilter(criticalActionLimiterService), JwtAuthenticationFilter.class);
	}
}
