package com.guidewire.devconnect;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntryPoint {
  private static Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  public static void main(String[] args) throws InterruptedException {
    LOGGER.info("Starting EntryPoint");

    ThreadFactory threadFactory = Executors.defaultThreadFactory();
    Thread producerThread = threadFactory.newThread(new ProducerRunner());
    Thread consumerThread = threadFactory.newThread(new ConsumerRunner());

    producerThread.start();
    consumerThread.start();

    producerThread.join();
    consumerThread.join();

    LOGGER.info("Starting EntryPoint");
  }
}
