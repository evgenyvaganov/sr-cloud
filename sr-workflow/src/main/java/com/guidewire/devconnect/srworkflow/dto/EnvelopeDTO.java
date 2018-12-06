package com.guidewire.devconnect.srworkflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EnvelopeDTO {
  private long _correlationId;
  private EnvelopePayloadType _type;
  private String _payload;

  public EnvelopeDTO(
    @JsonProperty("correlationId") long correlationId,
    @JsonProperty("type") EnvelopePayloadType type,
    @JsonProperty("payload") String payload) {
    _correlationId = correlationId;
    _type = type;
    _payload = payload;
  }

  public long getCorrelationId() {
    return _correlationId;
  }

  public EnvelopePayloadType getType() {
    return _type;
  }

  public String getPayload() {
    return _payload;
  }
}
