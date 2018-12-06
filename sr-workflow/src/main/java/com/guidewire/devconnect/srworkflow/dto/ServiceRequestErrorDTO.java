package com.guidewire.devconnect.srworkflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceRequestErrorDTO {
  private String _errorMsg;

  public ServiceRequestErrorDTO(@JsonProperty("errorMsg") String errorMsg) {
    _errorMsg = errorMsg;
  }

  public String getErrorMsg() {
    return _errorMsg;
  }
}
