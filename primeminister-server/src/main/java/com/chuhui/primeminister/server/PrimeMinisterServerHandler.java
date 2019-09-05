package com.chuhui.primeminister.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PrimeMinisterServerHandler
 *
 * @author: cyzi
 * @Date: 2019/9/2 0002
 * @Description:TODO
 */
public class PrimeMinisterServerHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private static final Logger LOG =
            LoggerFactory.getLogger(PrimeMinisterServerMain.class);


    public PrimeMinisterServerHandler() {
        LOG.info("PrimeMinisterServerHandler has initialized");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        LOG.info("msg:{}",   msg.toString(CharsetUtil.UTF_8));

        Channel channel = ctx.channel();
        channel.writeAndFlush("from server message:" + msg + System.lineSeparator());
    }



    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        LOG.info("a client connected...");
        super.handlerAdded(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        LOG.warn("invoke function  handlerRemoved,client:{}", ctx.channel().remoteAddress());
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOG.warn("invoke exceptionCaught,client:{}", ctx.channel().remoteAddress(), cause);
        ctx.close();
    }
}
