package com.ticket.monolithticketmonster.infrastructure.config;

import com.ticket.monolithticketmonster.domain.model.SnowflakeIdGenerator;
import java.net.UnknownHostException;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SnowflakeIdGeneratorInitializer
    implements ApplicationListener<WebServerInitializedEvent> {

  private int serverPort;

  @Override
  public void onApplicationEvent(WebServerInitializedEvent event) {
    this.serverPort = event.getWebServer().getPort();
  }

  @Bean
  public SnowflakeIdGenerator snowflakeIdGenerator() throws UnknownHostException {
    return new SnowflakeIdGenerator(serverPort);
  }
}
