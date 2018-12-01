package com.guidewire.devconnect.srworkflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceRequestDTO {
  private long _id;
  private String _state;

  public ServiceRequestDTO(@JsonProperty("id") long id, @JsonProperty("state") String state) {
    _id = id;
    _state = state;
  }

  public long getId() {
    return _id;
  }

  public String getState() {
    return _state;
  }
}
