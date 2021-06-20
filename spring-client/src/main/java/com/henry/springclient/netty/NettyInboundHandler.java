package com.henry.springclient.netty;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class NettyInboundHandler extends ChannelInboundHandlerAdapter {

  private ChannelHandlerContext ctx;
  private final Map<Long, CompletableFuture<Long>> futureMap;

  public NettyInboundHandler(Map<Long, CompletableFuture<Long>> futureMap) {
    this.futureMap = futureMap;
  }

  public boolean isConnected() {
    return this.ctx != null;
  }

  public void sendMessage(String msg) {
    if (ctx != null) {
      ctx.writeAndFlush(Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8));
    } else {
      throw new IllegalStateException("메세지를 보낼 수 없는 상태임");
    }
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    if (this.ctx != null) {
      this.ctx.close();
      this.ctx = null;
    }

    this.ctx = ctx;
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    try {
      ByteBuf buf = (ByteBuf) msg;
      String received = buf.toString(CharsetUtil.UTF_8);

      System.out.println("Received: " + received);

      // Parse Message
      String[] messages = received.split("\\|");
      if (messages.length < 2) {
        return;
      }

      Long requestId = Long.valueOf(messages[0]);
      Long luckyNum = Long.valueOf(messages[1]);

      // Release Future
      CompletableFuture<Long> future = futureMap.get(requestId);
      try {
        if (future == null) {
          System.err.println(String.format("RequestId: %s, LuckyNum: %s을 받았으나 반환할 곳을 찾을 수 없음", requestId, luckyNum));
          return;
        }

        future.complete(luckyNum);
      } finally {
        if (future != null && !future.isDone()) {
          future.cancel(true);
        }
        futureMap.remove(requestId);
      }
    } finally {
      ReferenceCountUtil.release(msg);
    }
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    this.ctx = null;
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    cause.printStackTrace();
    ctx.close();
  }
}
