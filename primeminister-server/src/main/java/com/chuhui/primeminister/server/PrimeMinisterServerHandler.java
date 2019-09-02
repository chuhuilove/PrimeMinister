package com.chuhui.primeminister.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PrimeMinisterServerHandler
 *
 * @author: cyzi
 * @Date: 2019/9/2 0002
 * @Description:TODO
 */
public class PrimeMinisterServerHandler extends SimpleChannelInboundHandler<String> {

    private static final Logger LOG =
            LoggerFactory.getLogger(PrimeMinisterServerMain.class);


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        LOG.info("msg:{}", msg);

        Channel channel = ctx.channel();

        channel.writeAndFlush("from server message:" + msg);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        LOG.info("a client connected...");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        ctx.channel().close();
    }
}
