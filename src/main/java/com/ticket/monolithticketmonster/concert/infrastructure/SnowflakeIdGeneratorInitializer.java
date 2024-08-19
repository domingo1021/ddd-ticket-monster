package com.ticket.monolithticketmonster.concert.infrastructure;


import com.ticket.monolithticketmonster.concert.domain.SnowflakeIdGenerator;
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
    return SnowflakeIdGenerator.getGenerator(serverPort);
  }
}
