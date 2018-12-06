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
import com.guidewire.devconnect.dto.ServiceRequestCreateDTO;
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

      // 1. Submitting service request
      LOGGER.info("1. Sending request to create SR");
      createServiceRequest(1);
      LOGGER.info("1. Successfully sent request to create SR");

      // 2. Waiting for the service request to be DRAFT
      LOGGER.info("2. Waiting for REQUESTED");
      ServiceRequestDTO serviceRequestDTO = waitForSnapshot(2, ServiceRequestStateDTO.DRAFT);
      LOGGER.info("2. Successfully received REQUESTED");

      // 3. Submitting instruction
      LOGGER.info("3. Submitting instruction");
      submitInstruction(3, serviceRequestDTO);
      LOGGER.info("3. Successfully submitted instruction");

      // 4. Waiting for the service request to be IN-PROGRESS
      LOGGER.info("4. Waiting for IN-PROGRESS");
      waitForSnapshot(0, ServiceRequestStateDTO.IN_PROGRESS);
      LOGGER.info("4. Successfully received IN-PROGRESS");

      // 5. Wait for the service request to be WORK-COMPLETE
      LOGGER.info("5. Waiting for WORK-COMPLETE");
      waitForSnapshot(0, ServiceRequestStateDTO.WORK_COMPLETE);
      LOGGER.info("5. Successfully received WORK-COMPLETE");

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

  private void submitInstruction(long correlationId, ServiceRequestDTO serviceRequestDTO)
      throws JsonProcessingException, InterruptedException, ExecutionException, TimeoutException {
    ServiceRequestUpdateDTO event = new ServiceRequestUpdateDTO(serviceRequestDTO.getId(), ServiceRequestEventDTO.SUBMIT_INSTRUCTION);
    String eventAsString = _mapper.writeValueAsString(event);
    EnvelopeDTO envelopeDTO = new EnvelopeDTO(correlationId, EnvelopePayloadType.SERVICE_REQUEST_UPDATE, eventAsString);
    String envelopeAsString = _mapper.writeValueAsString(envelopeDTO);

    ProducerRecord<Long, String> record = new ProducerRecord<>("carrier-out", envelopeAsString);

    Future<RecordMetadata> future = _producer.send(record);
    future.get(10, TimeUnit.SECONDS);
  }

  private void createServiceRequest(long correlationId)
      throws JsonProcessingException, InterruptedException, ExecutionException, TimeoutException {
    ServiceRequestCreateDTO event = new ServiceRequestCreateDTO("Replace transmission");
    String eventAsString = _mapper.writeValueAsString(event);
    EnvelopeDTO envelopeDTO = new EnvelopeDTO(correlationId, EnvelopePayloadType.SERVICE_REQUEST_CREATE, eventAsString);
    String envelopeAsString = _mapper.writeValueAsString(envelopeDTO);

    ProducerRecord<Long, String> record = new ProducerRecord<>("carrier-out", envelopeAsString);

    Future<RecordMetadata> future = _producer.send(record);
    future.get(10, TimeUnit.SECONDS);
  }

  private ServiceRequestDTO waitForSnapshot(long correlationId, ServiceRequestStateDTO state) throws IOException {
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
