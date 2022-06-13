package com.chuhui.primeminister.network;

import com.chuhui.primeminister.server.PrimeMinisterServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

import static com.chuhui.primeminister.constant.Constants.PMDBBUF;
import static com.chuhui.primeminister.constant.Constants.RESERVEPMDBBUF;

/**
 * NettyRemotingInitializer
 *
 * @author: cyzi
 * @Date: 2019/9/5 0005
 * @Description:TODO
 */
public class NettyRemotingInitializer extends ChannelInitializer<SocketChannel> {

    /**
     * 帧最大长度,64K
     */
    private static final int FRAME_MAX_LENGTH = 64 * 1024;


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        ChannelPipeline pipeline = ch.pipeline();
        //TODO 阅读LengthFieldBasedFrameDecoder
//        pipeline.addLast("in ",new LengthFieldBasedFrameDecoder(FRAME_MAX_LENGTH,))
        pipeline.addLast("in DelimiterBasedFrameDecoder <PMDB>", new DelimiterBasedFrameDecoder(FRAME_MAX_LENGTH, true, PMDBBUF, RESERVEPMDBBUF));

        pipeline.addLast("in primeMinisterServerHandler", new PrimeMinisterServerHandler());

    }
}
