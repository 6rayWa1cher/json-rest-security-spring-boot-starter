package com.a6raywa1cher.jsonrestsecurity.advancedapp;

import com.a6raywa1cher.jsonrestsecurity.web.JsonRestWebSecurityConfigurer;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
public class SecurityConfig extends JsonRestWebSecurityConfigurer {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/home/admin").hasAnyRole("ADMIN");

		super.configure(http);
	}
}
