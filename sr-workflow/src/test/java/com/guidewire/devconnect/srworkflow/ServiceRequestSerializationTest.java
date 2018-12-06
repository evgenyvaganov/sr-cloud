package com.guidewire.devconnect.srworkflow;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guidewire.devconnect.srworkflow.dto.ServiceRequestDTO;
import com.guidewire.devconnect.srworkflow.dto.ServiceRequestEventDTO;
import org.junit.Test;

public class ServiceRequestSerializationTest {
  @Test
  public void testServiceRequestToJson() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();

    ServiceRequestDTO serviceRequestDTO = new ServiceRequestDTO(25, "DRAFT", "Transmission is broken");
    ServiceRequestEventDTO eventDTO = new ServiceRequestEventDTO(10, serviceRequestDTO);

    String eventDTOAsString = objectMapper.writeValueAsString(eventDTO);
    ServiceRequestEventDTO deserializedEventDTO = objectMapper.readValue(eventDTOAsString, ServiceRequestEventDTO.class);
    assertThat(deserializedEventDTO.getCorrelationId()).isEqualTo(eventDTO.getCorrelationId());
    assertThat(deserializedEventDTO.getServiceRequestDTO().getId()).isEqualTo(eventDTO.getServiceRequestDTO().getId());
    assertThat(deserializedEventDTO.getServiceRequestDTO().getState()).isEqualTo(eventDTO.getServiceRequestDTO().getState());
    assertThat(deserializedEventDTO.getServiceRequestDTO().getDescription()).isEqualTo(eventDTO.getServiceRequestDTO().getDescription());
  }
}
