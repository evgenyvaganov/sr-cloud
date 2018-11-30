package com.guidewire.devconnect.srworkflow.queue;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventProcessor implements Runnable {
  private static Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  public void run() {
    try {
      for(int i = 0; i < 100; ++i) {
        Thread.sleep(1000);
        LOGGER.info("Processing events");
      }
    } catch (InterruptedException e) {
      LOGGER.error("The execution has been interrupted", e);
    }
  }
}
