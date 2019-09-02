package com.chuhui.primeminister.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.InetSocketAddress;

import static com.chuhui.primeminister.constant.Constants.DEFAULT_CONFIG_FILENAME;

/**
 * Boot
 *
 * @author: cyzi
 * @Date: 2019/9/2 0002
 * @Description:TODO
 */
public class PrimeMinisterServerMain {
    private static final Logger LOG =
            LoggerFactory.getLogger(PrimeMinisterServerMain.class);

    public static void main(String[] args) {

        PrimeMinisterServerMain serverMain = new PrimeMinisterServerMain();
        serverMain.initializeAndRun(args);
    }


    public void initializeAndRun(String[] args) {

        LOG.info("Starting server");

        // resolve configure file
        ServerConfig config = ServerConfig.getInstance();
        if (args.length == 1) {
            config.parse(args[0]);
        } else {
            InputStream stream = PrimeMinisterServerMain.class.getClassLoader().getResourceAsStream(DEFAULT_CONFIG_FILENAME);
            config.parse(stream);
        }

        // start listen server
        startServer(config);


    }

    public void startServer(ServerConfig config) {


        //配置服务端Nio线程
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("PrimeMinisterServerHandler", new PrimeMinisterServerHandler());
                        }
                    });


            // 绑定端口,同步等待成功
            int port = config.getConfigureModel().getServer().getPort();
            String host = config.getConfigureModel().getServer().getHost();

            InetSocketAddress inetSocketAddress = new InetSocketAddress(host, port);

            ChannelFuture f = b.bind(inetSocketAddress).sync();
            //等待服务端监听端口关闭
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            LOG.error("start server occurred error,server will exit", e);
            System.exit(1);
        } finally {
            // 优雅退出,释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }


}
