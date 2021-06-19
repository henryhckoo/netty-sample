package com.henry.nettyclient;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class NettyClient {

  public static void main(String[] args) {
    String host = "localhost";
    int port = 9999;

    EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

    try {
      Bootstrap bootstrap = new Bootstrap()
          .group(eventLoopGroup)
          .channel(NioSocketChannel.class)
          .option(ChannelOption.SO_KEEPALIVE, true)
          .handler(new LoggingHandler(LogLevel.INFO))
          .handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
              ch.pipeline().addLast(new LuckyNumberClientHandler());
            }
          });

      ChannelFuture future = bootstrap.connect(host, port).sync();

      future.channel().closeFuture().sync().channel();
    } catch (InterruptedException e) {
      System.err.println("Bye");
    } finally {
      eventLoopGroup.shutdownGracefully();
    }
  }

}
