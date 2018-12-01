package com.guidewire.devconnect.srworkflow;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guidewire.devconnect.srworkflow.dto.ServiceRequestDTO;
import org.junit.Test;

public class ServiceRequestSerializationTest {
  @Test
  public void testServiceRequestToJson() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();

    ServiceRequestDTO originalSR = new ServiceRequestDTO(10, "DRAFT");

    String srAsString = objectMapper.writeValueAsString(originalSR);
    ServiceRequestDTO deserializedSR = objectMapper.readValue(srAsString, ServiceRequestDTO.class);
    assertThat(deserializedSR.getId()).isEqualTo(originalSR.getId());
    assertThat(deserializedSR.getState()).isEqualTo(originalSR.getState());
  }
}
