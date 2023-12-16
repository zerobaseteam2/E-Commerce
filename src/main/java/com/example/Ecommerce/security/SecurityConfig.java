package com.example.Ecommerce.security;

import com.example.Ecommerce.security.jwt.JwtAuthenticationFilter;
import com.example.Ecommerce.security.jwt.JwtTokenUtil;
import com.example.Ecommerce.user.repository.LogoutAccessTokenRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
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
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
      throws Exception {
    return configuration.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public JwtAuthenticationFilter jwtAuthorizationFilter() {
    return new JwtAuthenticationFilter(jwtTokenUtil, userDetailService,
        logoutAccessTokenRedisRepository);
  }

  // h2 console Spring Security 제외 설정
  @Bean
  @ConditionalOnProperty(name = "spring.h2.console.enabled", havingValue = "true")
  public WebSecurityCustomizer configureH2ConsoleEnable() {
    return web -> web.ignoring()
        .requestMatchers(PathRequest.toH2Console());
  }

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
                .requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers("/api-docs/**").permitAll()
                .requestMatchers("/api/user/verify/{id}").permitAll()
                .requestMatchers("/api/user/address").hasRole("CUSTOMER")
                .requestMatchers("v1/product").hasRole("SELLER")
                .anyRequest().authenticated())
        .logout((httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer.disable()))
        .sessionManagement((sessionConfig) ->
            sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
