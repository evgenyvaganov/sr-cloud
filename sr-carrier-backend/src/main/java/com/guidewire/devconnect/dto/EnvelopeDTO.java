package com.guidewire.devconnect.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EnvelopeDTO {
  private EnvelopePayloadType _type;
  private String _payload;

  public EnvelopeDTO(
    @JsonProperty("type") EnvelopePayloadType type,
    @JsonProperty("payload") String payload) {
    _type = type;
    _payload = payload;
  }

  public EnvelopePayloadType getType() {
    return _type;
  }

  public String getPayload() {
    return _payload;
  }
}
