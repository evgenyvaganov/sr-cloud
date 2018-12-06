package com.guidewire.devconnect.srworkflow.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static java.lang.String.format;
import com.guidewire.devconnect.srworkflow.dto.ServiceRequestCreateDTO;
import com.guidewire.devconnect.srworkflow.dto.ServiceRequestUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;

public class ServiceRequestService {
  private final AtomicLong _srId = new AtomicLong(0);

  private final Map<Long, ServiceRequest> _srMap = new HashMap<>();

  @Autowired
  @SuppressWarnings("all")
  private StateMachineFactory<ServiceRequestState, ServiceRequestEvent> _stateMachineFactory;

  public ServiceRequest handle(ServiceRequestCreateDTO create) {
    StateMachine<ServiceRequestState, ServiceRequestEvent> stateMachine = _stateMachineFactory.getStateMachine();
    ServiceRequest serviceRequest = new ServiceRequest(_srId.getAndIncrement(), create.getDescription(), stateMachine);
    _srMap.put(serviceRequest.getId(), serviceRequest);
    return serviceRequest;
  }

  public ServiceRequest handle(ServiceRequestUpdateDTO update) throws IllegalTransitionException {
    ServiceRequest serviceRequest = _srMap.get(update.getId());
    if (serviceRequest == null) {
      throw new IllegalTransitionException(format("The service request with id %s has not been found", update));
    } else {
      ServiceRequestEvent event = ServiceRequestEvent.valueOf(update.getEvent());
      serviceRequest.transit(event);
      return serviceRequest;
    }
  }
}