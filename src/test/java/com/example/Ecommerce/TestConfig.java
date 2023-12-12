package com.example.Ecommerce;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@TestConfiguration
public class TestConfig {

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .httpBasic((httpBasicConfig) ->
            httpBasicConfig.disable())
        .csrf((csrfConfig) ->
            csrfConfig.disable())
        .formLogin((formLoginConfig) ->
            formLoginConfig.disable())
        .authorizeHttpRequests((auth) ->
            auth.requestMatchers("/api/user/register").permitAll()
                .requestMatchers("/api/user/login").permitAll()
                .anyRequest().authenticated())
        .sessionManagement((sessionConfig) ->
            sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    return http.build();
  }
}
