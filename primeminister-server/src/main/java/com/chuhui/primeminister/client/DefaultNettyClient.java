package com.chuhui.primeminister.client;


import com.chuhui.primeminister.network.NettyRemotingInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;

/**
 * DefaultNettyClient
 * 客户端默认实现
 *
 * @author: cyzi
 * @Date: 2019/9/5 0005
 * @Description:TODO
 */
public class DefaultNettyClient implements NettyClientInterface {

    private String host;
    private int port;

    public DefaultNettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void connection() {
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {


            Bootstrap b = new Bootstrap();
            b.group(workGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new NettyRemotingInitializer());

            InetSocketAddress socketAddress = new InetSocketAddress(host, port);

            Channel channel = b.connect(socketAddress).sync().channel();

            BufferedReader inputStreamReader = new BufferedReader(new InputStreamReader(System.in));

            for (; ; ) {
                System.err.println("please input data:");
                channel.writeAndFlush(inputStreamReader.readLine() + System.lineSeparator());
            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            workGroup.shutdownGracefully();
        }
    }
}
