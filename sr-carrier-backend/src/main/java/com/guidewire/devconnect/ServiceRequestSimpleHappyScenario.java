package com.guidewire.devconnect;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guidewire.devconnect.dto.EnvelopeDTO;
import com.guidewire.devconnect.dto.EnvelopePayloadType;
import com.guidewire.devconnect.dto.ServiceRequestEventDTO;
import com.guidewire.devconnect.queue.ConsumerCreator;
import com.guidewire.devconnect.queue.ProducerCreator;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceRequestSimpleHappyScenario implements Runnable {
  private static Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final Producer<Long, String> _producer;
  private final Consumer<Long, String> _consumer;
  private final ObjectMapper _mapper;

  public ServiceRequestSimpleHappyScenario() {
    _producer = ProducerCreator.createProducer();
    _consumer = ConsumerCreator.createConsumer();
    _mapper = new ObjectMapper();
  }

  @Override
  public void run() {
    LOGGER.info("Start SR simple happy path scenario");

    ServiceRequestEventDTO event = new ServiceRequestEventDTO(1, "DRAFT", "SPECIALIST_ACCEPTED_WORK");
    String eventAsString = _mapper.writeValueAsString(event);
    EnvelopeDTO envelopeDTO = new EnvelopeDTO(EnvelopePayloadType.SERVICE_REQUEST_EVENT, eventAsString);
    String envelopeAsString = _mapper.writeValueAsString(envelopeDTO);

    ProducerRecord<Long, String> record = new ProducerRecord<>("carrier-out", eventAsString);

    Future<RecordMetadata> sentFuture = _producer.send(record);

    LOGGER.info("Finished SR simple happy path scenario");
  }

  private void sendOut(Producer<Long, String> producer, String message) {
    try {
      RecordMetadata metadata = producer.send(record).get(1000, TimeUnit.SECONDS);
      LOGGER.info("Record sent with key {} to partition {}", index, metadata.partition()
        + " with offset " + metadata.offset());
    } catch (ExecutionException | InterruptedException | TimeoutException e) {
      LOGGER.error("Error in sending record", e);
    }
  }
}
