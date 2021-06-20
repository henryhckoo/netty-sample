package com.henry.springclient.web;

import com.henry.springclient.netty.NettyClient;
import java.util.concurrent.ExecutionException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LuckyController {

  private final NettyClient nettyClient;

  public LuckyController(NettyClient nettyClient) {
    this.nettyClient = nettyClient;
  }

  @GetMapping
  public String getLuckyNumber() throws ExecutionException, InterruptedException {
    return String.valueOf(nettyClient.getLuckyNum().get());
  }

}
