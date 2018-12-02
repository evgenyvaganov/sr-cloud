package com.guidewire.devconnect.srworkflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceRequestEventDTO {
  private long _id;
  private String _state;
  private String _event;

  public ServiceRequestEventDTO(
    @JsonProperty("id") long id,
    @JsonProperty("state") String state,
    @JsonProperty("event") String event) {
    _id = id;
    _state = state;
    _event = event;
  }

  public long getId() {
    return _id;
  }

  public String getState() {
    return _state;
  }

  public String getEvent() {
    return _event;
  }
}
