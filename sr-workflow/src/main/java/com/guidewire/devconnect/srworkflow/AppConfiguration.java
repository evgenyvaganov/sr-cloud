package com.guidewire.devconnect.srworkflow;

import com.guidewire.devconnect.srworkflow.queue.EventProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class AppConfiguration {
  @Bean
  public EventProcessor getEventProcessor() {
    return new EventProcessor();
  }
}
