package com.guidewire.devconnect.srworkflow.queue;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static java.lang.String.format;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guidewire.devconnect.srworkflow.domain.IllegalTransitionException;
import com.guidewire.devconnect.srworkflow.domain.ServiceRequestService;
import com.guidewire.devconnect.srworkflow.dto.ServiceRequestErrorDTO;
import com.guidewire.devconnect.srworkflow.dto.ServiceRequestEventDTO;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class EventProcessor implements Runnable {
  private static Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Autowired
  private ServiceRequestService _serviceRequestService;

  @Autowired
  private ObjectMapper _objectMapper;

  @Autowired
  private Producer<Long, String> _producer;

  @Autowired
  private Consumer<Long, String> _consumer;

  public void run() {
    LOGGER.info("Started Consumer");

    while (true) {
      ConsumerRecords<Long, String> consumerRecords = _consumer.poll(Duration.ofMillis(5000));
      if (consumerRecords.count() == 0) {
        LOGGER.info("No data");
        continue;
      }

      for (ConsumerRecord<Long, String> record : consumerRecords) {
        handle(record);
      }

      _consumer.commitAsync();
    }
  }

  private void handle(ConsumerRecord<Long, String> event) {
    String payload = event.value();
    ServiceRequestEventDTO dto = null;
    try {
      dto = _objectMapper.readValue(payload, ServiceRequestEventDTO.class);
      _serviceRequestService.handle(dto);
    } catch (IOException e) {
      LOGGER.error(format("The event %s has failed to parse", payload), e);
    } catch (IllegalTransitionException e) {
      LOGGER.error("Transition error {}", e.getMessage());
      handleTransitionError(event, dto, e.getMessage());
    }
  }

  private void handleTransitionError(ConsumerRecord<Long, String> event, ServiceRequestEventDTO dto, String errorMsg) {
    try {
      ServiceRequestErrorDTO error = new ServiceRequestErrorDTO(errorMsg, dto);
      String errorPayload = _objectMapper.writeValueAsString(error);
      publishEvent(event.topic(), errorPayload);
    } catch (JsonProcessingException e) {
      LOGGER.error(format("Could not process error %s and send event about it", errorMsg), e);
    }
  }

  private void publishEvent(String topic, String payload) {
    ProducerRecord<Long, String> record = new ProducerRecord<>(topic, payload);
    try {
      RecordMetadata metadata = _producer.send(record).get(1000, TimeUnit.SECONDS);
      LOGGER.info("Record sent to partition {}  with offset {}", metadata.partition(), metadata.offset());
    } catch (ExecutionException | InterruptedException | TimeoutException e) {
      LOGGER.error("Error in sending record", e);
    }
  }
}
