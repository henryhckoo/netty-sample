package com.henry.nettyclient;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import java.util.concurrent.atomic.AtomicLong;

public class LuckyNumberClientHandler extends ChannelInboundHandlerAdapter {

  private final AtomicLong ticket = new AtomicLong(0);

  @Override
  public void channelActive(ChannelHandlerContext ctx) {
    ChannelFuture a = ctx.writeAndFlush(
        Unpooled.copiedBuffer(
            String.valueOf(ticket.getAndIncrement()), CharsetUtil.UTF_8
        )
    );

    if (!a.isSuccess()) {
      System.err.println("Can't send");
    }

  }


  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    try {
      ByteBuf buf = (ByteBuf) msg;
      System.out.println("Received: " + buf.toString(CharsetUtil.UTF_8));
    } finally {
      ReferenceCountUtil.release(msg);
    }
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
