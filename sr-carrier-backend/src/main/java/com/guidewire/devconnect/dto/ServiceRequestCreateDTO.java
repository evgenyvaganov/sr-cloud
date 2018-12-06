package com.guidewire.devconnect.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceRequestCreateDTO {
  private String _description;

  public ServiceRequestCreateDTO(@JsonProperty("description") String description) {
    _description = description;
  }

  public String getDescription() {
    return _description;
  }
}
