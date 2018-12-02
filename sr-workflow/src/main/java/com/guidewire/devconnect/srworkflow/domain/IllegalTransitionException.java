package com.guidewire.devconnect.srworkflow.domain;

public class IllegalTransitionException extends Exception {
  public IllegalTransitionException(String message) {
    super(message);
  }
}
