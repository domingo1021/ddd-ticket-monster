package com.ticket.monolithticketmonster.user.infrastructure.config;

import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GracefulShutdown {

  private static final Logger logger = LoggerFactory.getLogger(GracefulShutdown.class);

  @PreDestroy
  public void onShutdown() {
    logger.info("Application is shutting down, flushing logs...");
  }
}
