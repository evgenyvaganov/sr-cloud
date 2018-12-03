package com.guidewire.devconnect.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceRequestErrorDTO {
  private String _errorMsg;
  private ServiceRequestEventDTO _event;

  public ServiceRequestErrorDTO(
    @JsonProperty("errorMsg") String errorMsg,
    @JsonProperty("event") ServiceRequestEventDTO event) {
    _errorMsg = errorMsg;
    _event = event;
  }

  public String getErrorMsg() {
    return _errorMsg;
  }

  public ServiceRequestEventDTO getEvent() {
    return _event;
  }
}
