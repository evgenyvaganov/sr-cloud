package com.guidewire.devconnect;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guidewire.devconnect.dto.EnvelopeDTO;
import com.guidewire.devconnect.dto.EnvelopePayloadType;
import com.guidewire.devconnect.dto.ServiceRequestDTO;
import com.guidewire.devconnect.dto.ServiceRequestEventDTO;
import com.guidewire.devconnect.dto.ServiceRequestStateDTO;
import com.guidewire.devconnect.dto.ServiceRequestUpdateDTO;
import com.guidewire.devconnect.queue.ConsumerCreator;
import com.guidewire.devconnect.queue.ProducerCreator;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("all")
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
    try {
      LOGGER.info("Start SR simple happy path scenario");

      // 1. Waiting for SR REQUESTED
      LOGGER.info("1. Waiting event REQUESTED");
      ServiceRequestDTO serviceRequestDTO = waitForSnapshot(null, ServiceRequestStateDTO.REQUESTED);
      LOGGER.info("1. Successfully recieved REQUESTED event");

      // 2. Submitting Specialist Accepted Work
      LOGGER.info("2. Submitting Specialist Accepted Work and waiting it to be IN-PROGRESS");
      postUpdate(serviceRequestDTO, 2, ServiceRequestEventDTO.SPECIALIST_ACCEPTED_WORK);
      serviceRequestDTO = waitForSnapshot(2L, ServiceRequestStateDTO.IN_PROGRESS);
      LOGGER.info("2. Successfully submitted Specialist Accepted Work and received IN-PROGRESS event");

      // 3. Submitting Specialist Accepted Work
      LOGGER.info("3. Submitting Specialist Completed Work and waiting it to be WORK-COMPLETE");
      postUpdate(serviceRequestDTO, 3, ServiceRequestEventDTO.SPECIALIST_COMPLETED_WORK);
      serviceRequestDTO = waitForSnapshot(3L, ServiceRequestStateDTO.WORK_COMPLETE);
      LOGGER.info("3. Successfully submitted Specialist Completed Work and received WORK-COMPLETE event");

      LOGGER.info("Finished SR simple happy path scenario");
    } catch (JsonProcessingException e) {
      LOGGER.error("Failed to convert some of the messages", e);
    } catch (InterruptedException e) {
      LOGGER.error("The execution has been interrupted");
    } catch (ExecutionException | TimeoutException e) {
      LOGGER.error("Failed to send out the request", e);
    } catch (IOException e) {
      LOGGER.error("Failed to read the data of the response", e);
    }
  }

  private void postUpdate(ServiceRequestDTO serviceRequestDTO, long correlationId, ServiceRequestEventDTO eventDTO)
      throws JsonProcessingException, InterruptedException, ExecutionException, TimeoutException {
    ServiceRequestUpdateDTO event = new ServiceRequestUpdateDTO(serviceRequestDTO.getId(), eventDTO);
    String eventAsString = _mapper.writeValueAsString(event);
    EnvelopeDTO envelopeDTO = new EnvelopeDTO(correlationId, EnvelopePayloadType.SERVICE_REQUEST_UPDATE, eventAsString);
    String envelopeAsString = _mapper.writeValueAsString(envelopeDTO);

    ProducerRecord<Long, String> record = new ProducerRecord<>("carrier-out", envelopeAsString);

    Future<RecordMetadata> future = _producer.send(record);
    future.get(10, TimeUnit.SECONDS);
  }

  private ServiceRequestDTO waitForSnapshot(Long correlationId, ServiceRequestStateDTO state) throws IOException {
    ConsumerRecords<Long, String> consumerRecords = _consumer.poll(Duration.ofMillis(5000));
    ConsumerRecord<Long, String> response = consumerRecords.iterator().next();
    _consumer.commitAsync();

    EnvelopeDTO envelopeDTO = _mapper.readValue(response.value(), EnvelopeDTO.class);
    if (envelopeDTO.getCorrelationId() != correlationId || envelopeDTO.getType() !=  EnvelopePayloadType.SERVICE_REQUEST_SNAPSHOT) {
      String errorMsg = String.format("The correlationId of the envelope should be %s and type %s",
        correlationId, EnvelopePayloadType.SERVICE_REQUEST_SNAPSHOT);
      throw new IllegalStateException(errorMsg);
    }

    ServiceRequestDTO serviceRequestDTO = _mapper.readValue(envelopeDTO.getPayload(), ServiceRequestDTO.class);
    if (serviceRequestDTO.getState() != state) {
      String errorMsg = String.format("The state of the service request should be %s, but it is %s",
        state, serviceRequestDTO.getState());
      throw new IllegalStateException(errorMsg);
    }

    LOGGER.info("2. Received ServiceRequestDTO: [id={}, state={}, description={}]",
      serviceRequestDTO.getId(), serviceRequestDTO.getState(), serviceRequestDTO.getDescription());

    return serviceRequestDTO;
  }
}
