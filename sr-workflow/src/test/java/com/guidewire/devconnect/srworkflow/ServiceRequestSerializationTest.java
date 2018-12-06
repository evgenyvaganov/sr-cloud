package com.guidewire.devconnect.srworkflow;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guidewire.devconnect.srworkflow.dto.ServiceRequestDTO;
import com.guidewire.devconnect.srworkflow.dto.ServiceRequestSnapshotDTO;
import com.guidewire.devconnect.srworkflow.dto.ServiceRequestStateDTO;
import org.junit.Test;

public class ServiceRequestSerializationTest {
  @Test
  public void testServiceRequestToJson() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();

    ServiceRequestDTO serviceRequestDTO = new ServiceRequestDTO(25, ServiceRequestStateDTO.DRAFT, "Transmission is broken");
    ServiceRequestSnapshotDTO eventDTO = new ServiceRequestSnapshotDTO(10, serviceRequestDTO);

    String eventDTOAsString = objectMapper.writeValueAsString(eventDTO);
    ServiceRequestSnapshotDTO deserializedEventDTO = objectMapper.readValue(eventDTOAsString, ServiceRequestSnapshotDTO.class);
    assertThat(deserializedEventDTO.getCorrelationId()).isEqualTo(eventDTO.getCorrelationId());
    assertThat(deserializedEventDTO.getServiceRequestDTO().getId()).isEqualTo(eventDTO.getServiceRequestDTO().getId());
    assertThat(deserializedEventDTO.getServiceRequestDTO().getState()).isEqualTo(eventDTO.getServiceRequestDTO().getState());
    assertThat(deserializedEventDTO.getServiceRequestDTO().getDescription()).isEqualTo(eventDTO.getServiceRequestDTO().getDescription());
  }
}
