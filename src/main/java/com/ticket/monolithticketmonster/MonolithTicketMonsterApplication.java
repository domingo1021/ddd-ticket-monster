package com.ticket.monolithticketmonster;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MonolithTicketMonsterApplication {

  public static void main(String[] args) {
    // Load env before app start
    Dotenv dotenv = Dotenv.configure().load();
    dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

    SpringApplication.run(MonolithTicketMonsterApplication.class, args);
  }
}
