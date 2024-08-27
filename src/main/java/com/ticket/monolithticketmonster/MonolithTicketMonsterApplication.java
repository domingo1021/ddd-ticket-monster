package com.ticket.monolithticketmonster;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.AbstractEnvironment;

@SpringBootApplication
public class MonolithTicketMonsterApplication {

  public static void main(String[] args) {
    // Load .env file if SPRING_PROFILES_ACTIVE is not set (assuming it's the local environment)
    String activeProfile = System.getenv("SPRING_PROFILES_ACTIVE");
    if (activeProfile == null || activeProfile.isEmpty()) {
      Dotenv dotenv = Dotenv.configure().load();
      dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
    }

    SpringApplication.run(MonolithTicketMonsterApplication.class, args);
  }
}
