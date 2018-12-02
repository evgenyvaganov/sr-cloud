package com.guidewire.devconnect.srworkflow.domain;

import java.util.HashMap;
import java.util.Map;

import com.guidewire.devconnect.srworkflow.dto.ServiceRequestEventDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.config.StateMachineFactory;

public class ServiceRequestService {
  private final Map<Long, ServiceRequest> _srMap = new HashMap<>();

  @Autowired
  @SuppressWarnings("all")
  private StateMachineFactory<ServiceRequestState, ServiceRequestEvent> _stateMachineFactory;


  public void handle(ServiceRequestEventDTO dto) throws IllegalTransitionException {

  }
}
