package com.ticket.monolithticketmonster.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
  private final RequestAuthenticationWhitelist requestAuthenticationWhitelist;
  private final JwtFilter jwtFilter;

  public SecurityConfig(
      RequestAuthenticationWhitelist requestAuthenticationWhitelist, JwtFilter jwtFilter) {
    this.requestAuthenticationWhitelist = requestAuthenticationWhitelist;
    this.jwtFilter = jwtFilter;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            requests -> {
              requestAuthenticationWhitelist
                  .getAuthenticationMatcher()
                  .forEach(
                      (method, patterns) -> {
                        patterns.forEach(
                            pattern -> requests.requestMatchers(method, pattern).permitAll());
                      });
              requests.anyRequest().authenticated();
            })
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
