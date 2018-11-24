package com.guidewire.devconnect;

import java.time.Duration;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;

public class ConsumerMain {
  public static void main(String[] args) {
    Consumer<Long, String> consumer = ConsumerCreator.createConsumer();

    while (true) {
      ConsumerRecords<Long, String> consumerRecords = consumer.poll(Duration.ofMillis(5000));
      // 1000 is the time in milliseconds consumer will wait if no record is found at broker.
      if (consumerRecords.count() == 0) {
        System.out.println("No data");
        continue;
      }
      //print each record.
      consumerRecords.forEach(record -> {
        System.out.println("Record Key " + record.key());
        System.out.println("Record value " + record.value());
        System.out.println("Record partition " + record.partition());
        System.out.println("Record offset " + record.offset());
      });
      // commits the offset of record to broker.
      consumer.commitAsync();
    }
  }
}