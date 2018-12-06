package com.guidewire.devconnect.srworkflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceRequestErrorDTO {
  private long _correlationId;
  private String _errorMsg;

  public ServiceRequestErrorDTO(
    @JsonProperty("correlationId") long correlationId,
    @JsonProperty("errorMsg") String errorMsg) {
    _correlationId = correlationId;
    _errorMsg = errorMsg;
  }

  public long getCorrelationId() {
    return _correlationId;
  }

  public String getErrorMsg() {
    return _errorMsg;
  }
}
