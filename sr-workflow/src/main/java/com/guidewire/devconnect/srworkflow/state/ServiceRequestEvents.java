package com.guidewire.devconnect.srworkflow.state;

public enum ServiceRequestEvents {
  SPECIALIST_ACCEPTED_WORK(1, "Vendor Accepted Work"),
  SPECIALIST_RESUMED_WORK(2, "Vendor Resumed Work"),
  SPECIALIST_COMPLETED_WORK(3, "Vendor Completed Work"),
  SPECIALIST_WAITING(4, "Vendor Waiting"),
  ADD_QUOTE(5, "Add Quote"),
  ADD_INVOICE(6, "Add Invoice"),
  SPECIALIST_DECLINED(7, "Vendor Declined"),
  SPECIALIST_CANCELED(8, "Vendor Canceled Service"),
  SUBMIT_INSTRUCTION(9, "Submit Instruction"),
  APPROVE_QUOTE(10, "Approve Quote"),
  REQUEST_REQUOTE(11, "Request Requote"),
  UPDATE_QUOTE_ECD(12, "Update Quote ECD"),
  UPDATE_SERVICE_ECD(13, "Update Service ECD"),
  CANCEL_SERVICE_REQUEST(14, "Cancel Service"),
  APPROVE_INVOICE(15, "Approve Invoice"),
  PAY_INVOICE(16, "Pay Invoice"),
  REJECT_INVOICE(17, "Reject Invoice"),
  WITHDRAW_INVOICE(18, "Withdraw Invoice"),
  UNPAY_INVOICE(19, "Unpay Invoice");

  private final int _code;
  private final String _description;

  ServiceRequestEvents(int code, String description) {
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