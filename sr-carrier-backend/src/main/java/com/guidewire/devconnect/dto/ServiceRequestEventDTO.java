package com.guidewire.devconnect.dto;

public enum ServiceRequestEventDTO {
  SUBMIT_INSTRUCTION("submit-instruction", "Submit Instruction"),
  SPECIALIST_ACCEPTED_WORK("specialist-accepted-work", "Vendor Accepted Work"),
  SPECIALIST_COMPLETED_WORK("specialist-completed-work", "Vendor Completed Work");

  private final String _code;
  private final String _description;

  ServiceRequestEventDTO(String code, String description) {
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