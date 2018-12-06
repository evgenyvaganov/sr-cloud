package com.guidewire.devconnect.srworkflow.domain;

import static java.lang.String.format;
import org.springframework.statemachine.StateMachine;

public class ServiceRequest {
  private final long _id;
  private final String _description;
  private final StateMachine<ServiceRequestState, ServiceRequestEvent> _stateMachine;

  public ServiceRequest(long id, String description, StateMachine<ServiceRequestState, ServiceRequestEvent> stateMachine) {
    _id = id;
    _description = description;
    _stateMachine = stateMachine;
  }

  public long getId() {
    return _id;
  }

  public ServiceRequestState getState() {
    return _stateMachine.getState().getId();
  }

  public String getDescription() {
    return _description;
  }

  public void transit(ServiceRequestEvent event) throws IllegalTransitionException {
    if(!_stateMachine.sendEvent(event)) {
      throw new IllegalTransitionException(format("The event %s has not been accepted", event));
    }
  }
}
