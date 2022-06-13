package com.chuhui.primeminister.core.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.InetSocketAddress;

/**
 * NettyService
 *
 * @author: cyzi
 * @Date: 6/4/22
 * @Description:
 */

@Slf4j
@Component
public class NetworkServer implements ApplicationListener<StartNetworkEvent> {

    @Resource
    private NettyConfiguration nettyConfiguration;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ServerBootstrap bootstrap;


    private void start() {
        try {

            bossGroup = new NioEventLoopGroup();
            workerGroup = new NioEventLoopGroup();
            bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new NetworkReadInitializer());

            int port = nettyConfiguration.getPort();
            String host = nettyConfiguration.getHost();

            InetSocketAddress inetSocketAddress = new InetSocketAddress(host, port);
            ChannelFuture f = bootstrap.bind(inetSocketAddress).sync();
            log.info("netty server started");
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("启动服务失败");
            throw new RuntimeException("启动服务失败");
        } finally {
            // 优雅退出,释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }


    @Override
    public void onApplicationEvent(StartNetworkEvent event) {
        start();
    }
}
