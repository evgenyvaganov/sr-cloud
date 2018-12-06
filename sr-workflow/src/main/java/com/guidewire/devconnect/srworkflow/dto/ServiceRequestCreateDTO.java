package com.guidewire.devconnect.srworkflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceRequestCreateDTO {
  private long _correlationId;
  private String _description;

  public ServiceRequestCreateDTO(
    @JsonProperty("correlationId") long correlationId,
    @JsonProperty("description") String description) {
    _correlationId = correlationId;
    _description = description;
  }

  public long getCorrelationId() {
    return _correlationId;
  }

  public String getDescription() {
    return _description;
  }
}
