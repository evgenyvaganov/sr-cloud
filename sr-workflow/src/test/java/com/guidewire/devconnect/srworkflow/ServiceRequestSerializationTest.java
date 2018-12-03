package com.guidewire.devconnect.srworkflow;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guidewire.devconnect.srworkflow.dto.ServiceRequestEventDTO;
import org.junit.Test;

public class ServiceRequestSerializationTest {
  @Test
  public void testServiceRequestToJson() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();

    ServiceRequestEventDTO originalSR = new ServiceRequestEventDTO(
      10, "DRAFT", "SPECIALIST_ACCEPTED_WORK");

    String srAsString = objectMapper.writeValueAsString(originalSR);
    ServiceRequestEventDTO deserializedSR = objectMapper.readValue(srAsString, ServiceRequestEventDTO.class);
    assertThat(deserializedSR.getId()).isEqualTo(originalSR.getId());
    assertThat(deserializedSR.getCurrentState()).isEqualTo(originalSR.getCurrentState());
  }
}
