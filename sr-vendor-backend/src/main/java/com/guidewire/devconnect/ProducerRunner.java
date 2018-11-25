package com.guidewire.devconnect;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProducerRunner implements Runnable {
  private static Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  public void run() {
    LOGGER.info("Producer started");

    Producer<Long, String> producer = ProducerCreator.createProducer();
    for (int index = 0; index < 1000; ++index) {
      ProducerRecord<Long, String> record = new ProducerRecord<>("vendor-out", "This is record " + index);
      try {
        RecordMetadata metadata = producer.send(record).get(1000, TimeUnit.SECONDS);
        LOGGER.info("Record sent with key {} to partition {} with offset {}", index, metadata.partition(), metadata.offset());
      } catch (ExecutionException | InterruptedException | TimeoutException e) {
        LOGGER.info("Error in sending record", e);
      }
    }

    LOGGER.info("Producer finished");
  }
}