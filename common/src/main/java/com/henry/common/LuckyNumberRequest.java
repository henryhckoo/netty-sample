package com.henry.common;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LuckyNumberRequest {

  private final long requestId;

  public LuckyNumberRequest(long requestId) {
    this.requestId = requestId;
  }

  @Override
  public String toString() {
    return "{"
        + "\"requestId\": " + requestId
        + "}";
  }

}
