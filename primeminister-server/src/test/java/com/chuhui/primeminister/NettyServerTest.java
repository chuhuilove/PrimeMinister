package com.chuhui.primeminister;

import com.chuhui.primeminister.server.PrimeMinisterServerMain;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.EventExecutorGroup;
import org.junit.Before;
import org.junit.Test;

/**
 * NettyServerTest
 *
 * @author: cyzi
 * @Date: 2019/9/2 0002
 * @Description:TODO
 */
public class NettyServerTest {

    private PrimeMinisterServerMain serverMain;


    @Before
    public void initliazer() {
        serverMain = new PrimeMinisterServerMain();
        serverMain.initializeAndRun(null);
    }


    @Test
    public void connectToServer() {
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {


            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new CustomeStringMessageHandler());
                        }
                    });

            ChannelFuture future = bootstrap.bind(13225).sync();
            future.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workGroup.shutdownGracefully();
        }

    }


    private class CustomeStringMessageHandler extends SimpleChannelInboundHandler<String> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
        }
    }
}
