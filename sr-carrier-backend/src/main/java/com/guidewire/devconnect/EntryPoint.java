package com.guidewire.devconnect;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import com.guidewire.devconnect.queue.ConsumerRunner;
import com.guidewire.devconnect.queue.ProducerRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntryPoint {
  private static Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  public static void main(String[] args) throws InterruptedException {
    LOGGER.info("Starting EntryPoint");

    ThreadFactory threadFactory = Executors.defaultThreadFactory();
    Thread scenarioThread = threadFactory.newThread(new ServiceRequestSimpleHappyScenario());

    scenarioThread.start();
    scenarioThread.join();

    LOGGER.info("Starting EntryPoint");
  }
}
