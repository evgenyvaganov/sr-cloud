package com.guidewire.devconnect;

import java.lang.invoke.MethodHandles;
import java.time.Duration;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsumerRunner implements Runnable {
  private static Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  public void run() {
    LOGGER.info("Started Consumer");

    Consumer<Long, String> consumer = ConsumerCreator.createConsumer();

    try {
      while (true) {
        ConsumerRecords<Long, String> consumerRecords = consumer.poll(Duration.ofMillis(5000));
        // 1000 is the time in milliseconds consumer will wait if no record is found at broker.
        if (consumerRecords.count() == 0) {
          LOGGER.info("No data");
          continue;
        }
        //print each record.
        consumerRecords.forEach(record -> {
          LOGGER.info("Record Key {}", record.key());
          LOGGER.info("Record value {}", record.value());
          LOGGER.info("Record partition {}", record.partition());
          LOGGER.info("Record offset {}", record.offset());
        });
        // commits the offset of record to broker.
        consumer.commitAsync();
      }
    } catch (Exception e) {
      LOGGER.info("Finished (was interrupted) Consumer", e);
    }
  }
}