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
import com.guidewire.devconnect.srworkflow.domain.ServiceRequest;
import com.guidewire.devconnect.srworkflow.domain.ServiceRequestService;
import com.guidewire.devconnect.srworkflow.dto.DomainToDTOConverter;
import com.guidewire.devconnect.srworkflow.dto.EnvelopeDTO;
import com.guidewire.devconnect.srworkflow.dto.EnvelopePayloadType;
import com.guidewire.devconnect.srworkflow.dto.ServiceRequestCreateDTO;
import com.guidewire.devconnect.srworkflow.dto.ServiceRequestDTO;
import com.guidewire.devconnect.srworkflow.dto.ServiceRequestErrorDTO;
import com.guidewire.devconnect.srworkflow.dto.ServiceRequestEventDTO;
import com.guidewire.devconnect.srworkflow.dto.ServiceRequestUpdateDTO;
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

  @Autowired
  private DomainToDTOConverter _converter;

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
    try {
      EnvelopeDTO envelope = _objectMapper.readValue(payload, EnvelopeDTO.class);
      switch (envelope.getType()) {
        case SERVICE_REQUEST_CREATE: {
          ServiceRequestCreateDTO create = _objectMapper.readValue(envelope.getPayload(), ServiceRequestCreateDTO.class);
          ServiceRequest serviceRequest = _serviceRequestService.handle(create);

          String envelopeDTOAsString = toEvent(serviceRequest, create.getCorrelationId());

          publishEvent("carrier-in", envelopeDTOAsString);
          publishEvent("vendor-in", envelopeDTOAsString);
          break;
        }
        case SERVICE_REQUEST_UPDATE: {
          ServiceRequestUpdateDTO update = _objectMapper.readValue(envelope.getPayload(), ServiceRequestUpdateDTO.class);
          try {
            ServiceRequest serviceRequest = _serviceRequestService.handle(update);
            String envelopeDTOAsString = toEvent(serviceRequest, update.getCorrelationId());

            publishEvent("carrier-in", envelopeDTOAsString);
            publishEvent("vendor-in", envelopeDTOAsString);
          } catch (IllegalTransitionException e) {
            LOGGER.error("Transition error {}", e.getMessage());
            handleTransitionError(event, update.getCorrelationId(), e.getMessage());
          }
          break;
        }
        case SERVICE_REQUEST_EVENT:
        case SERVICE_REQUEST_EVENT_ERROR:
          throw new IllegalArgumentException(format("The event %s is incorrect for inbound request", envelope.getType()));
        default:
          throw new IllegalArgumentException(format("The envelope type %s isn't supported", envelope.getType()));
      }
    } catch (IOException e) {
      LOGGER.error(format("The event %s has failed to parse", payload), e);
    } catch (Exception e) {
      LOGGER.error("Could not able to process event", e);
    }
  }

  private String toEvent(ServiceRequest serviceRequest, long correlationId) throws JsonProcessingException {
    ServiceRequestDTO serviceRequestDTO = _converter.toDTO(serviceRequest);
    ServiceRequestEventDTO eventDTO = new ServiceRequestEventDTO(correlationId, serviceRequestDTO);
    EnvelopeDTO envelopeDTO = new EnvelopeDTO(EnvelopePayloadType.SERVICE_REQUEST_EVENT, _objectMapper.writeValueAsString(eventDTO));
    return _objectMapper.writeValueAsString(envelopeDTO);
  }

  private void handleTransitionError(ConsumerRecord<Long, String> event, long correlationId, String errorMsg) throws JsonProcessingException {
    ServiceRequestErrorDTO error = new ServiceRequestErrorDTO(correlationId, errorMsg);
    String errorPayload = _objectMapper.writeValueAsString(error);
    EnvelopeDTO envelopeDTO = new EnvelopeDTO(EnvelopePayloadType.SERVICE_REQUEST_EVENT_ERROR, errorPayload);
    String errorEnvelope = _objectMapper.writeValueAsString(envelopeDTO);
    publishEvent(event.topic(), errorEnvelope);
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
