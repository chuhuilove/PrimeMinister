package com.chuhui.primeminister.core.network;

import com.chuhui.primeminister.common.network.NettyUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * PrimeMinisterNetworkServer
 *
 * @author: cyzi
 * @Date: 6/4/22
 * @Description:
 */

@Slf4j
public class PrimeMinisterNetworkServer {


    private static volatile boolean hasInitialized = false;

    private final NettyConfiguration nettyConfiguration;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    private final NettyNetworkInitializer initializer = new NettyNetworkInitializer();

    private Channel parentChannel;

    public PrimeMinisterNetworkServer(NettyConfiguration configuration) {

        if (hasInitialized) {
            throw new UnsupportedOperationException("已经创建了对象了,禁止再次创建");
        }

        this.nettyConfiguration = configuration;
        hasInitialized = true;
    }


    private void start() {
//        try {

        bossGroup = NettyUtils.platformEventLoopGroup(0);
        workerGroup = NettyUtils.platformEventLoopGroup(0);

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NettyUtils.platformServerSocketChannel())
//                .childOption()
                .childHandler(initializer);

        int port = nettyConfiguration.getPort();
        String host = nettyConfiguration.getHost();


        InetSocketAddress inetSocketAddress = new InetSocketAddress(host, port);
        ChannelFuture f = bootstrap.bind(inetSocketAddress)
                .addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
                        if (channelFuture.isSuccess()) {
                            InetSocketAddress socketAddress = (InetSocketAddress) channelFuture.channel().localAddress();
                            // 如果指定的端口为0,实际绑定后,端口会更改
                            log.info("bing address and port succeeded,address is:{},port is:{}!", socketAddress.getHostString(), socketAddress.getPort());
                            nettyConfiguration.setPort(socketAddress.getPort());
                        } else {
                            Throwable cause = channelFuture.cause();
                            log.error("bind address failed.", cause);
                        }
                    }
                });
        parentChannel = f.syncUninterruptibly().channel();
    }


    public Channel getParentChannel() {
        return parentChannel;
    }


    public void startUp() {
        start();
    }


}
