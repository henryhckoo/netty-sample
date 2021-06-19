package com.henry.nettyclient;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.util.concurrent.atomic.AtomicLong;

public class LuckyNumberClientHandler extends SimpleChannelInboundHandler<String> {

  private final AtomicLong ticket = new AtomicLong(1);

  @Override
  public void channelActive(ChannelHandlerContext ctx) {
    System.out.println("Channel activated");
    ctx.writeAndFlush("hi" + "\n");
  }


  @Override
  protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
    System.out.println(msg);
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) {
    ctx.flush();
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    cause.printStackTrace();
    ctx.close();
  }
}
