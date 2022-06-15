package com.chuhui.primeminister.core.network;

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
public class TransClassNameServerHandler extends SimpleChannelInboundHandler<ProtobufTest.TransClassName> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProtobufTest.TransClassName msg) throws Exception {

        String appName = msg.getAppName();
        ProtobufTest.DataType dataTpe = msg.getDataTpe();
        String msg1 = msg.getMsg();

        System.err.println("server read from client msg is,appName:"+appName+",dataTpe:"+dataTpe+",msg:"+msg1);

        ProtobufTest.TransClassName.Builder response = ProtobufTest.TransClassName.newBuilder(msg)
                .setMsg("这是服务端的响应");

        ctx.writeAndFlush(response);
    }
}
