package com.chuhui.primeminister.network;

import com.chuhui.primeminister.server.PrimeMinisterServerHandler;
import com.sun.corba.se.impl.interceptors.PICurrent;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

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
    private static final int  FRAME_MAX_LENGTH=64*1024;
    private static final String DELIMITER="<PMDB>";
    private static final String RESERVE_DELIMITER="</PMDB>";






    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        ByteBuf pmdbBuf = Unpooled.copiedBuffer(DELIMITER.getBytes());
        ByteBuf reservePmdbBuf = Unpooled.copiedBuffer(RESERVE_DELIMITER.getBytes());

        ChannelPipeline pipeline = ch.pipeline();
        //TODO 阅读LengthFieldBasedFrameDecoder
        pipeline.addLast("in ",new LengthFieldBasedFrameDecoder(FRAME_MAX_LENGTH,))
        pipeline.addLast("in DelimiterBasedFrameDecoder <PMDB>",new DelimiterBasedFrameDecoder(FRAME_MAX_LENGTH,true,pmdbBuf,reservePmdbBuf));
        pipeline.addLast("in primeMinisterServerHandler", new PrimeMinisterServerHandler());

    }
}
