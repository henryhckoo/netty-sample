package com.henry.springclient.web;

import com.henry.springclient.netty.NettyClient;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LuckyController {

  private final NettyClient nettyClient;

  public LuckyController(NettyClient nettyClient) {
    this.nettyClient = nettyClient;
  }

  @GetMapping
  public String getLuckyNumber() throws ExecutionException, InterruptedException, TimeoutException {
    CompletableFuture<Long> future = nettyClient.getLuckyNum();

    return String.valueOf(future.get(5000, TimeUnit.MILLISECONDS));
  }

}
