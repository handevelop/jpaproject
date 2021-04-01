package com.example.jpa.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // WebSecurityConfigurerAdapter 를 상속 받아서 컨피그를 오버라이딩
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf ().disable ();
        http.headers ().frameOptions ().sameOrigin ();

        http.authorizeRequests ().anyRequest ().permitAll ();
    }
}
