package com.guidewire.devconnect.srworkflow.dto;

import static java.lang.String.format;
import com.guidewire.devconnect.srworkflow.domain.ServiceRequest;
import com.guidewire.devconnect.srworkflow.domain.ServiceRequestEvent;
import com.guidewire.devconnect.srworkflow.domain.ServiceRequestState;

public class DomainToDTOConverter {
  public ServiceRequestDTO toDTO(ServiceRequest serviceRequest) {
    ServiceRequestStateDTO stateDTO = toDTO(serviceRequest.getState());

    return new ServiceRequestDTO(serviceRequest.getId(), stateDTO, serviceRequest.getDescription());
  }

  private ServiceRequestStateDTO toDTO(ServiceRequestState state) {
    switch (state) {
      case DRAFT:
        return ServiceRequestStateDTO.DRAFT;
      case REQUESTED:
        return ServiceRequestStateDTO.REQUESTED;
      case IN_PROGRESS:
        return ServiceRequestStateDTO.IN_PROGRESS;
      case WORK_COMPLETE:
        return ServiceRequestStateDTO.WORK_COMPLETE;
      default:
        throw new IllegalArgumentException(format("The state %s is not supported", state));
    }
  }

  public ServiceRequestEvent toDomain(ServiceRequestEventDTO event) {
    switch (event) {
      case SUBMIT_INSTRUCTION:
        return ServiceRequestEvent.SUBMIT_INSTRUCTION;
      case SPECIALIST_ACCEPTED_WORK:
        return ServiceRequestEvent.SPECIALIST_ACCEPTED_WORK;
      case SPECIALIST_COMPLETED_WORK:
        return ServiceRequestEvent.SPECIALIST_COMPLETED_WORK;
      default:
        throw new IllegalArgumentException(format("The event %s is unsupported", event.getCode()));
    }
  }
}
