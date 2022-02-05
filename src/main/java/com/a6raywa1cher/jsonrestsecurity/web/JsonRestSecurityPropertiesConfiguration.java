package com.a6raywa1cher.jsonrestsecurity.web;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableConfigurationProperties(JsonRestSecurityConfigProperties.class)
@PropertySource("classpath:jrs-application.properties")
public class JsonRestSecurityPropertiesConfiguration {
}
