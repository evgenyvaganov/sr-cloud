package com.guidewire.devconnect.srworkflow;

import java.lang.invoke.MethodHandles;

import com.guidewire.devconnect.srworkflow.queue.EventProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SrWorkflowApplication {
  private static Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  public static void main(String[] args) {
    ConfigurableApplicationContext context = SpringApplication.run(SrWorkflowApplication.class, args);
    EventProcessor eventProcessor = context.getBean(EventProcessor.class);

    Thread thread = new Thread(eventProcessor);
    thread.setDaemon(true);
    thread.start();

    try {
      LOGGER.info("Start sleeping");
      Thread.sleep(5000);
      LOGGER.info("Finished sleeping");
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
