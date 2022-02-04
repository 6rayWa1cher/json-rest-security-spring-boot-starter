package com.a6raywa1cher.websecurityspringbootstarter.web;

import com.a6raywa1cher.websecurityspringbootstarter.component.WebSecurityComponentsConfiguration;
import com.a6raywa1cher.websecurityspringbootstarter.criticalaction.CriticalActionLimiterConfiguration;
import com.a6raywa1cher.websecurityspringbootstarter.dao.WebSecurityDaoConfiguration;
import com.a6raywa1cher.websecurityspringbootstarter.jwt.JwtAuthConfiguration;
import com.a6raywa1cher.websecurityspringbootstarter.rest.AuthControllerConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
	PasswordEncoderConfiguration.class,
	DefaultWebSecurityConfigurer.class,
	AuthControllerConfiguration.class,
	JwtAuthConfiguration.class,
	WebSecurityComponentsConfiguration.class,
	WebSecurityDaoConfiguration.class,
	CriticalActionLimiterConfiguration.class,
	WebSecurityPropertiesConfiguration.class
})
public class WebSecurityAutoConfiguration {

}
