package com.a6raywa1cher.websecurityspringbootstarter.web;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(WebSecurityConfigProperties.class)
public class WebSecurityPropertiesConfiguration {
}
