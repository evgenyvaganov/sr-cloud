package com.guidewire.devconnect.srworkflow.domain;

import org.springframework.statemachine.StateMachine;

public class ServiceRequest {
  private final long _id;
  private final StateMachine<ServiceRequestState, ServiceRequestEvent> _stateMachine;

  public ServiceRequest(long id, StateMachine<ServiceRequestState, ServiceRequestEvent> stateMachine) {
    _id = id;
    _stateMachine = stateMachine;
  }

  public long getId() {
    return _id;
  }

  public ServiceRequestState getStateMachine() {
    return _stateMachine.getState().getId();
  }
}
