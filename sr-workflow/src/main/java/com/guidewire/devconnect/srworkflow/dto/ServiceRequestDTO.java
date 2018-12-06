package com.guidewire.devconnect.srworkflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceRequestDTO {
  private long _id;
  private String _state;
  private String _description;

  public ServiceRequestDTO(
    @JsonProperty("id") long id,
    @JsonProperty("state") String state,
    @JsonProperty("description") String description) {
    _id = id;
    _state = state;
    _description = description;
  }

  public long getId() {
    return _id;
  }

  public String getState() {
    return _state;
  }

  public String getDescription() {
    return _description;
  }
}
