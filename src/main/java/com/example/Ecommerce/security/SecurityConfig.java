package com.example.Ecommerce.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final JwtTokenUtil jwtTokenUtil;
  private final UserDetailServiceImpl userDetailService;
  private final LogoutAccessTokenRedisRepository logoutAccessTokenRedisRepository;
  
  
  // 인증처리를 위한 AuthenticaitonManager
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
    return configuration.getAuthenticationManager();
  }
  
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

//  @Bean
//  public JwtAuthenticationFilter jwtAuthorizationFilter() {
//    return new JwtAuthenticationFilter(jwtTokenUtil, userDetailService, logoutAccessTokenRedisRepository);
//  }
  
  @Bean
  protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
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

//  @Bean
//  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//    auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder());
//  }
}
