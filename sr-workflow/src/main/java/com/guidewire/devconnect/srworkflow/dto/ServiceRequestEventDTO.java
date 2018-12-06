package com.guidewire.devconnect.srworkflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceRequestEventDTO {
  private long _correlationId;
  private ServiceRequestDTO _serviceRequestDTO;

  public ServiceRequestEventDTO(
    @JsonProperty("correlationId") long correlationId,
    @JsonProperty("serviceRequestDTO") ServiceRequestDTO serviceRequestDTO) {
    _correlationId = correlationId;
    _serviceRequestDTO = serviceRequestDTO;
  }

  public long getCorrelationId() {
    return _correlationId;
  }

  public ServiceRequestDTO getServiceRequestDTO() {
    return _serviceRequestDTO;
  }
}
