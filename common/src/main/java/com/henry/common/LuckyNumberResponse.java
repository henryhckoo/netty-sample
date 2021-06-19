package com.henry.common;

import java.util.concurrent.ThreadLocalRandom;

public class LuckyNumberResponse {

  private final long requestId;
  private final int luckyNum;

  public LuckyNumberResponse(long requestId) {
    this.requestId = requestId;
    this.luckyNum = ThreadLocalRandom.current().nextInt(1, 9999);
  }

  @Override
  public String toString() {
    return "{"
        + "\"requestId\": " + requestId + ", "
        + "\"luckyNum\": " + luckyNum
        + "}";
  }
}
