package com.henry.springclient.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Component;

@Component
public class NettyClient implements Runnable {

  private final AtomicLong ticket = new AtomicLong(0);
  private final Map<Long, CompletableFuture<Long>> futureMap = new ConcurrentHashMap<>(128);

  private final String host;
  private final int port;

  private final NettyInboundHandler nettyInboundHandler;
  private ExecutorService executorService = null;


  public NettyClient() {
    this.nettyInboundHandler = new NettyInboundHandler(futureMap);
    this.host = "localhost";
    this.port = 9999;

    startClient();
  }

  private void startClient() {
    if (executorService != null) {
      executorService.shutdown();
    }

    executorService = Executors.newFixedThreadPool(1);
    executorService.execute(this);
  }

  @Override
  public void run() {
    EventLoopGroup workerGroup = new NioEventLoopGroup();

    try {
      Bootstrap b = new Bootstrap()
          .group(workerGroup)
          .channel(NioSocketChannel.class)
          .option(ChannelOption.SO_KEEPALIVE, true)
          .handler(this.nettyInboundHandler);

      ChannelFuture f = b.connect(host, port).sync();

      f.channel().closeFuture().sync();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      workerGroup.shutdownGracefully();
    }
  }

  public CompletableFuture<Long> getLuckyNum() {
    if (!nettyInboundHandler.isConnected()) {
      throw new IllegalStateException("메세지를 보낼 수 없는 상태임");
    }

    // Future 생성 및 관리
    long requestId = ticket.getAndIncrement();
    CompletableFuture<Long> future = new CompletableFuture<Long>();
    futureMap.put(requestId, future);

    // 메세지 보내기
    nettyInboundHandler.sendMessage(String.valueOf(requestId));

    return future;
  }
}
