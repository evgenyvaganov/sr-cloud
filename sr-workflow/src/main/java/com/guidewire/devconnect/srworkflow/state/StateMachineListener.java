package com.guidewire.devconnect.srworkflow.state;

import org.springframework.messaging.Message;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

public final class StateMachineListener extends StateMachineListenerAdapter<ServiceRequestStates, ServiceRequestEvents> {
  @Override
  public void stateChanged(State<ServiceRequestStates, ServiceRequestEvents> from, State<ServiceRequestStates, ServiceRequestEvents> to) {
    System.out.println("Order state changed to " + to.getId());
  }

  @Override
  public void eventNotAccepted(Message<ServiceRequestEvents> event) {
    System.out.println("Event is not accepted " + event.getHeaders());
  }
}