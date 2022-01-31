package com.a6raywa1cher.websecurityspringbootstarter;

import com.a6raywa1cher.websecurityspringbootstarter.config.SecurityConfig;
import com.a6raywa1cher.websecurityspringbootstarter.config.WebSecurityConfigProperties;
import com.a6raywa1cher.websecurityspringbootstarter.config.WebSecurityConfigurer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({
        WebSecurityConfigProperties.class
})
@AutoConfigureAfter({
        WebMvcAutoConfiguration.class,
        JpaRepositoriesAutoConfiguration.class,
        SecurityAutoConfiguration.class,
        SecurityFilterAutoConfiguration.class
})
@Import({WebSecurityConfigurer.class, SecurityConfig.class})
public class WebSecurityAutoConfiguration {

}
