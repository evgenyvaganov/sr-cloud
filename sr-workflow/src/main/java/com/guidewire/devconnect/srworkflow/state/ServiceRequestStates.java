package com.guidewire.devconnect.srworkflow.state;

public enum ServiceRequestStates {
  DRAFT(1, "The service is still being edited and has not yet been sent to the vendor"),
  REQUESTED(2, "Service request has been sent to the selected vendor, and has been acknowledged"),
  SPECIALIST_WAITING(3, "Vendor is blocked"),
  IN_PROGRESS(4, "The vendor is authorized to perform the work"),
  WORK_COMPLETE(5, "The work has been completed"),
  DECLINED(6, "The vendor has declined to accept the service request"),
  CANCELED(7, "The service request has been canceled"),
  EXPIRED(8, "The service has expired");

  private final int _code;
  private final String _description;

  ServiceRequestStates(int code, String description) {
    _code = code;
    _description = description;
  }

  public int getCode() {
    return _code;
  }

  public String getDescription() {
    return _description;
  }
}