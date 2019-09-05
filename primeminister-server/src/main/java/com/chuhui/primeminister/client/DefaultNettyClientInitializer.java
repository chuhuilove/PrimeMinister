package com.chuhui.primeminister.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

import static com.chuhui.primeminister.constant.Constants.PMDBBUF;
import static com.chuhui.primeminister.constant.Constants.RESERVEPMDBBUF;

/**
 * DefaultNettyClientInitializer
 *
 * @author: cyzi
 * @Date: 2019/9/5 0005
 * @Description:TODO
 */
public class DefaultNettyClientInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new DelimiterBasedFrameDecoder(4096,PMDBBUF,RESERVEPMDBBUF));
        pipeline.addLast(new DefaultNettyClientHandler());
    }
}
