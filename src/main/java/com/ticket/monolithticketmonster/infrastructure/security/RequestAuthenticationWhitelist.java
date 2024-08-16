package com.ticket.monolithticketmonster.infrastructure.security;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@ConfigurationProperties(prefix = "whitelist")
public class RequestAuthenticationWhitelist {

  @Setter
  private Map<String, List<String>> requestMatchers;

  @Getter
  private Map<HttpMethod, List<String>> authenticationMatcher;

  @PostConstruct
  public void init() {
    System.out.println(this.requestMatchers);
    if (requestMatchers == null) {
      throw new IllegalStateException("Endpoints map is not initialized. Check your configuration.");
    }

    this.authenticationMatcher = requestMatchers.entrySet().stream()
        .collect(Collectors.toMap(
            entry -> HttpMethod.valueOf(entry.getKey()),
            Map.Entry::getValue
        ));
  }
}
