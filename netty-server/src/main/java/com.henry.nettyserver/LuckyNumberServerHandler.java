package com.henry.nettyserver;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public class LuckyNumberServerHandler extends ChannelInboundHandlerAdapter {

  @Override
  public void channelActive(ChannelHandlerContext ctx) {
    ctx.writeAndFlush(Unpooled.copiedBuffer("hi", CharsetUtil.UTF_8));
  }


  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    try {
      ByteBuf in = (ByteBuf) msg;
      String msgString = in.toString(CharsetUtil.UTF_8);
      System.out.println("Received: " + msgString);

      int randomNum = ThreadLocalRandom.current().nextInt(1, 9999);
      String response = msgString + "|" + randomNum;

      ctx.writeAndFlush(Unpooled.copiedBuffer(response, CharsetUtil.UTF_8));
    } finally {
      ReferenceCountUtil.release(msg);
    }
  }


  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    if (cause instanceof IOException) {
      System.out.println("Someone just left");
    } else {
      cause.printStackTrace();
    }

    ctx.close();
  }
}
