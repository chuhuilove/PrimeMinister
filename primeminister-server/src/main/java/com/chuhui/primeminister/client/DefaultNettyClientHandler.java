package com.chuhui.primeminister.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import org.omg.CORBA.CTX_RESTRICT_SCOPE;
import org.omg.PortableServer.SERVANT_RETENTION_POLICY_ID;

import java.util.UUID;

/**
 * DefaultNettyClientHandler
 *
 * @author: cyzi
 * @Date: 2019/9/5 0005
 * @Description:TODO
 */
public class DefaultNettyClientHandler extends SimpleChannelInboundHandler<ByteBuf> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        System.err.println(msg.toString(CharsetUtil.UTF_8));
        ctx.writeAndFlush(Unpooled.copiedBuffer(("<PMDB> from client message:"+UUID.randomUUID().toString()+"</PMDB>").getBytes()));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer(("<PMDB> first client:"+ UUID.randomUUID().toString() +"</PMDB>").getBytes()));
    }

}
