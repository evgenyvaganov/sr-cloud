package com.guidewire.devconnect.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceRequestEventDTO {
  private long _id;
  private String _state;
  private String _event;

  public ServiceRequestEventDTO(
    @JsonProperty("id") long id,
    @JsonProperty("currentState") String state,
    @JsonProperty("event") String event) {
    _id = id;
    _state = state;
    _event = event;
  }

  public long getId() {
    return _id;
  }

  public String getCurrentState() {
    return _state;
  }

  public String getEvent() {
    return _event;
  }
}