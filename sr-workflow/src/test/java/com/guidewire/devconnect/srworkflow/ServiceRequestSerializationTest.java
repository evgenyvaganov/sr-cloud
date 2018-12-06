package com.guidewire.devconnect.srworkflow;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guidewire.devconnect.srworkflow.dto.ServiceRequestDTO;
import com.guidewire.devconnect.srworkflow.dto.ServiceRequestStateDTO;
import org.junit.Test;

public class ServiceRequestSerializationTest {
  @Test
  public void testServiceRequestToJson() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();

    ServiceRequestDTO requestDTO = new ServiceRequestDTO(25, ServiceRequestStateDTO.DRAFT, "Transmission is broken");

    String eventDTOAsString = objectMapper.writeValueAsString(requestDTO);
    ServiceRequestDTO deserializedRequestDTO = objectMapper.readValue(eventDTOAsString, ServiceRequestDTO.class);
    assertThat(deserializedRequestDTO.getId()).isEqualTo(requestDTO.getId());
    assertThat(deserializedRequestDTO.getState()).isEqualTo(requestDTO.getState());
    assertThat(deserializedRequestDTO.getDescription()).isEqualTo(requestDTO.getDescription());
  }
}
