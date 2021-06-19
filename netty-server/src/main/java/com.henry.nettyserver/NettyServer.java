package com.henry.nettyserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {

  private final int port;

  public NettyServer(int port) {
    this.port = port;
  }

  public void run() {
    EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

    try {
      ServerBootstrap bootstrap = new ServerBootstrap()
          .group(eventLoopGroup)
          .channel(NioServerSocketChannel.class)
          .childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
              ch.pipeline().addLast(new LuckyNumberServerHandler());
            }
          })
          .option(ChannelOption.SO_BACKLOG, 128)
          .childOption(ChannelOption.SO_KEEPALIVE, true);

      ChannelFuture future = bootstrap.bind(port).sync();

      System.out.println("Begin server");
      future.channel().closeFuture().sync();
    } catch (InterruptedException e) {
      System.err.println("Bye");
    } finally {
      eventLoopGroup.shutdownGracefully();
    }
  }

  public static void main(String[] args) {
    int port = 9999;
    if (args.length > 0) {
      port = Integer.parseInt(args[0]);
    }

    new NettyServer(port).run();
  }

}
