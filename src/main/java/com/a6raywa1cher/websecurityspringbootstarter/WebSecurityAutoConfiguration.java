package com.a6raywa1cher.websecurityspringbootstarter;

import com.a6raywa1cher.websecurityspringbootstarter.config.WebSecurityConfigProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.a6raywa1cher.websecurityspringbootstarter")
@EnableConfigurationProperties({
        WebSecurityConfigProperties.class
})
public class WebSecurityAutoConfiguration {

}
