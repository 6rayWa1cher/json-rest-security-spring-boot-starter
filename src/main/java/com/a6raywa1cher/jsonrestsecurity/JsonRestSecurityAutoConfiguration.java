package com.a6raywa1cher.jsonrestsecurity;

import com.a6raywa1cher.jsonrestsecurity.component.SecurityComponentsConfiguration;
import com.a6raywa1cher.jsonrestsecurity.criticalaction.CriticalActionLimiterConfiguration;
import com.a6raywa1cher.jsonrestsecurity.dao.DaoConfiguration;
import com.a6raywa1cher.jsonrestsecurity.jwt.JwtAuthConfiguration;
import com.a6raywa1cher.jsonrestsecurity.rest.AuthControllerConfiguration;
import com.a6raywa1cher.jsonrestsecurity.web.JsonRestSecurityPropertiesConfiguration;
import com.a6raywa1cher.jsonrestsecurity.web.JsonRestWebSecurityConfigurer;
import com.a6raywa1cher.jsonrestsecurity.web.PasswordEncoderConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConditionalOnProperty(prefix = "json-rest-security", name = "enable", havingValue = "true",
	matchIfMissing = true)
@Import({
	PasswordEncoderConfiguration.class,
	JsonRestWebSecurityConfigurer.class,
	AuthControllerConfiguration.class,
	JwtAuthConfiguration.class,
	SecurityComponentsConfiguration.class,
	DaoConfiguration.class,
	CriticalActionLimiterConfiguration.class,
	JsonRestSecurityPropertiesConfiguration.class
})
public class JsonRestSecurityAutoConfiguration {

}
