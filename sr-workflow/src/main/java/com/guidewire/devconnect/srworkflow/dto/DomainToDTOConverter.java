package com.guidewire.devconnect.srworkflow.dto;

import com.guidewire.devconnect.srworkflow.domain.ServiceRequest;

public class DomainToDTOConverter {
  public ServiceRequestDTO toDTO(ServiceRequest serviceRequest) {
    return new ServiceRequestDTO(serviceRequest.getId(), serviceRequest.getState().name(), serviceRequest.getDescription());
  }
}
