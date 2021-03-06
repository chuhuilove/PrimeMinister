package com.chuhui.primeminister.network;

import com.chuhui.primeminister.server.PrimeMinisterServerMain;
import com.chuhui.primeminister.server.ServerConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * NettyServer
 *
 * @author: cyzi
 * @Date: 2019/9/2 0002
 * @Description:TODO
 */
public class NettyRemotingServer extends AbstractNettyRemoting {

    private static final Logger LOG =
            LoggerFactory.getLogger(PrimeMinisterServerMain.class);


    private ServerConfig serverConfig;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    private ServerBootstrap bootstrap;

    public NettyRemotingServer(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    @Override
    public void start() {
        LOG.info("start event loop");

        try {


            bossGroup = new NioEventLoopGroup();
            workerGroup = new NioEventLoopGroup();
            bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new NettyRemotingInitializer());

            int port = serverConfig.getConfigureModel().getServer().getPort();
            String host = serverConfig.getConfigureModel().getServer().getHost();

            InetSocketAddress inetSocketAddress = new InetSocketAddress(host, port);

            ChannelFuture f = bootstrap.bind(inetSocketAddress).sync();
            //等待服务端监听端口关闭
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 优雅退出,释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }


}
