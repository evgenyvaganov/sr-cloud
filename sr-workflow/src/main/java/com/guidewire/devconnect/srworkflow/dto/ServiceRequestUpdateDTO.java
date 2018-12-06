package com.guidewire.devconnect.srworkflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceRequestUpdateDTO {
  private long _id;
  private ServiceRequestEventDTO _event;

  public ServiceRequestUpdateDTO(
    @JsonProperty("id") long id,
    @JsonProperty("event") ServiceRequestEventDTO event) {
    _id = id;
    _event = event;
  }

  public long getId() {
    return _id;
  }

  public ServiceRequestEventDTO getEvent() {
    return _event;
  }
}
