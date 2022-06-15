package com.chuhui.primeminister.collector.network;

import com.chuhui.primeminister.common.protocol.test.ProtobufTest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * TransClassNameClientHandler
 *
 * @author: cyzi
 * @Date: 6/15/22
 * @Description:
 */
public class TransClassNameClientHandler extends SimpleChannelInboundHandler<ProtobufTest.TransClassName> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProtobufTest.TransClassName msg) throws Exception {
        System.err.println("client read from server msg is:"+msg.toString());
    }
}
