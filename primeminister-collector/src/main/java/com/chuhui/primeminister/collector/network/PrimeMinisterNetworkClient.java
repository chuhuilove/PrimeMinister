package com.chuhui.primeminister.collector.network;

import com.chuhui.primeminister.common.network.NettyUtils;
import com.chuhui.primeminister.common.protocol.test.ProtobufTest;
import com.google.protobuf.ByteString;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * PrimeMinisterNetworkServer
 *
 * @author: cyzi
 * @Date: 6/4/22
 * @Description:
 */

@Slf4j
public class PrimeMinisterNetworkClient {


    private final String host = "127.0.0.1";
    private final int port = 12123;
    private Channel channel;


    public PrimeMinisterNetworkClient() {
        connect();
    }

    private void connect() {

        InetSocketAddress addr = new InetSocketAddress(host, port);
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup eventLoopGroup = NettyUtils.platformEventLoopGroup(1);
        bootstrap
                .group(eventLoopGroup)
                .channel(NettyUtils.platformSocketChannel())
                .handler(new NettyClientInitializer());

        bootstrap.validate();

        bootstrap.connect(addr)
                .addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture channelFuture) throws Exception {

                        if (channelFuture.isSuccess()) {
                            channel = channelFuture.channel();
                            System.err.println("连接到服务端成功,host:" + host + ",port:" + port);
                        } else {
                            System.err.println("创建连接失败");
                            channelFuture.cause().printStackTrace();
                        }
                    }
                });
    }

    public void sendMsg(String className) {
        try {
            // 发送数据到服务端


            ProtobufTest.TransClassName msgObj = ProtobufTest.TransClassName.newBuilder()
                    .setMsgBytes(ByteString.copyFromUtf8("转换的类是:" + className))
                    .setAppName("cyzi-selffeature-trace-first")
                    .setDataTpe(ProtobufTest.DataType.transMsg)
                    .build();


            channel.writeAndFlush(msgObj);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
