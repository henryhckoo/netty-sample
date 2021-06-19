package com.henry.common;

public class LuckyNumberRequest {

  private final long requestId;

  public LuckyNumberRequest(long requestId) {
    this.requestId = requestId;
  }

  public long getRequestId() {
    return requestId;
  }

  @Override
  public String toString() {
    return "{"
        + "\"requestId\": " + requestId
        + "}";
  }

}
