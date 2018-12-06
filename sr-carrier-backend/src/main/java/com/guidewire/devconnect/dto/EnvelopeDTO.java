package com.guidewire.devconnect.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EnvelopeDTO {
  private Long _correlationId;
  private EnvelopePayloadType _type;
  private String _payload;

  public EnvelopeDTO(
    @JsonProperty("correlationId") Long correlationId,
    @JsonProperty("type") EnvelopePayloadType type,
    @JsonProperty("payload") String payload) {
    _correlationId = correlationId;
    _type = type;
    _payload = payload;
  }

  public Long getCorrelationId() {
    return _correlationId;
  }

  public EnvelopePayloadType getType() {
    return _type;
  }

  public String getPayload() {
    return _payload;
  }
}
