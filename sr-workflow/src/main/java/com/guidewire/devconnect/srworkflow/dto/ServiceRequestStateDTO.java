package com.guidewire.devconnect.srworkflow.dto;

public enum ServiceRequestStateDTO {
  DRAFT("draft", "The service is still being edited and has not yet been sent to the vendor"),
  REQUESTED("requested", "Service request has been sent to the selected vendor, and has been acknowledged"),
  IN_PROGRESS("in-progress", "The vendor is authorized to perform the work"),
  WORK_COMPLETE("work-complete", "The work has been completed");

  private final String _code;
  private final String _description;

  ServiceRequestStateDTO(String code, String description) {
    _code = code;
    _description = description;
  }

  public String getCode() {
    return _code;
  }

  public String getDescription() {
    return _description;
  }
}