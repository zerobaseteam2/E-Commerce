package com.example.Ecommerce;

import com.example.Ecommerce.security.UserDetailServiceImpl;
import com.example.Ecommerce.security.jwt.JwtAuthenticationFilter;
import com.example.Ecommerce.security.jwt.JwtTokenUtil;
import com.example.Ecommerce.user.repository.LogoutAccessTokenRedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@TestConfiguration
@EnableWebSecurity
public class TestConfig {
  @Autowired
  private JwtAuthenticationFilter jwtAuthenticationFilter;
  @MockBean
  private JwtTokenUtil jwtTokenUtil;
  @MockBean
  private UserDetailServiceImpl userDetailService;
  @MockBean
  private LogoutAccessTokenRedisRepository logoutAccessTokenRedisRepository;
  
  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }
  
  @Bean
  public JwtAuthenticationFilter jwtAuthorizationFilter() {
    return new JwtAuthenticationFilter(jwtTokenUtil, userDetailService, logoutAccessTokenRedisRepository);
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
            .logout((httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer.disable()))
            .sessionManagement((sessionConfig) ->
                    sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    
    return http.build();
  }
}