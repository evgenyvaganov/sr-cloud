package com.guidewire.devconnect.srworkflow.queue;

import java.lang.invoke.MethodHandles;
import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventProcessor implements Runnable {
  private static Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  public void run() {
    LOGGER.info("Started Consumer");

    Consumer<Long, String> consumer = ConsumerCreator.createConsumer("carrier-out, vendor-out");

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
      LOGGER.info("Interrupted Consumer", e);
    }
  }

  private void publishEvent(Producer<Long, String> producer, String topic, String payload) {
    ProducerRecord<Long, String> record = new ProducerRecord<>(topic, payload);
    try {
      RecordMetadata metadata = producer.send(record).get(1000, TimeUnit.SECONDS);
      LOGGER.info("Record sent to partition {}  with offset {}", metadata.partition(), metadata.offset());
    } catch (ExecutionException | InterruptedException | TimeoutException e) {
      LOGGER.error("Error in sending record", e);
    }
  }
}
