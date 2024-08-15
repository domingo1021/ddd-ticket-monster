package com.ticket.monolithticketmonster.infrastructure.security;

import com.ticket.monolithticketmonster.application.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final UserService userService;
  private final JwtProvider jwtTokenProvider;
  private final AuthenticationEntryPoint authenticationEntryPoint;

  public SecurityConfig(
      UserService userService,
      JwtProvider jwtTokenProvider,
      AuthenticationEntryPoint authenticationEntryPoint) {
    this.userService = userService;
    this.jwtTokenProvider = jwtTokenProvider;
    this.authenticationEntryPoint = authenticationEntryPoint;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            requests ->
                requests
                    .requestMatchers(HttpMethod.POST, "/auth/signup", "/auth/signin")
                    .permitAll()
                    .requestMatchers(
                        HttpMethod.GET, "/tickets/**", "/oauth2/redirect/**", "/oauth2/callback/**")
                    .permitAll()
                    .requestMatchers(HttpMethod.POST, "/tickets/**")
                    .authenticated()
                    .requestMatchers("/ws/**")
                    .authenticated() // Also authenticate websocket HTTP handshake
                    .anyRequest()
                    .authenticated())
        .addFilterBefore(
            new JwtFilter(jwtTokenProvider, userService, authenticationEntryPoint),
            UsernamePasswordAuthenticationFilter
                .class); // The last filter will not reach, only used for setting Jwt Filter before
    // other filter.

    return http.build();
  }
}
