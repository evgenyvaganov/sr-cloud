package com.guidewire.devconnect.srworkflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceRequestUpdateDTO {
  private long _correlationId;
  private long _id;
  private String _event;

  public ServiceRequestUpdateDTO(
    @JsonProperty("correlationId") long correlationId,
    @JsonProperty("id") long id,
    @JsonProperty("event") String event) {
    _correlationId = correlationId;
    _id = id;
    _event = event;
  }

  public long getCorrelationId() {
    return _correlationId;
  }

  public long getId() {
    return _id;
  }

  public String getEvent() {
    return _event;
  }
}
