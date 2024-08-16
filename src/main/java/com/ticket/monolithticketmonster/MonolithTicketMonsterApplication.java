package com.ticket.monolithticketmonster;

import com.ticket.monolithticketmonster.infrastructure.security.RequestAuthenticationWhitelist;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RequestAuthenticationWhitelist.class)
public class MonolithTicketMonsterApplication {

  public static void main(String[] args) {
    // Load env before app start
    Dotenv dotenv = Dotenv.configure().load();
    dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

    SpringApplication.run(MonolithTicketMonsterApplication.class, args);
  }
}
