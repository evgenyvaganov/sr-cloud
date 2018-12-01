package com.guidewire.devconnect.srworkflow.domain;

import org.springframework.messaging.Message;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

public final class StateMachineListener extends StateMachineListenerAdapter<ServiceRequestState, ServiceRequestEvent> {
  @Override
  public void stateChanged(State<ServiceRequestState, ServiceRequestEvent> from, State<ServiceRequestState, ServiceRequestEvent> to) {
    System.out.println("Order state changed to " + to.getId());
  }

  @Override
  public void eventNotAccepted(Message<ServiceRequestEvent> event) {
    System.out.println("Event is not accepted " + event.getHeaders());
  }
}