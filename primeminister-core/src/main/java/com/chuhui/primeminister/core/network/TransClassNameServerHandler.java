package com.chuhui.primeminister.core.network;

import com.chuhui.primeminister.common.protocol.test.ProtobufTest;
import com.google.protobuf.ByteString;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * TransClassNameClientHandler
 *
 * @author: cyzi
 * @Date: 6/15/22
 * @Description:
 */
public class TransClassNameServerHandler extends SimpleChannelInboundHandler<ProtobufTest.TransClassName> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProtobufTest.TransClassName msg) throws Exception {


        System.err.println("server read from msg is:"+msg.toString());

        ProtobufTest.TransClassName.Builder response = ProtobufTest.TransClassName.newBuilder(msg)
                .setMsgBytes(ByteString.copyFromUtf8("这是服务端的响应"));

        ctx.writeAndFlush(response);
    }
}
