package com.chuhui.primeminister.core.network;

import com.chuhui.primeminister.common.protocol.test.ProtobufTest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.kqueue.KQueueSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;


/**
 * NettyRemotingInitializer
 *
 * @author: cyzi
 * @Date: 2019/9/5 0005
 * @Description:
 */
public class NettyNetworkInitializer extends ChannelInitializer<SocketChannel> {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
    }


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        if(ch instanceof KQueueSocketChannel){
            initChannelKqueue((KQueueSocketChannel) ch);
        }
        if(ch instanceof EpollSocketChannel){
            initChannelEpoll((EpollSocketChannel) ch);
        }
        if(ch instanceof NioSocketChannel){
            initChannelNio((NioSocketChannel) ch);
        }

        ch.pipeline()
                .addLast(new ProtobufVarint32FrameDecoder())
                .addLast(new ProtobufDecoder(ProtobufTest.TransClassName.getDefaultInstance()))
                .addLast(new ProtobufVarint32LengthFieldPrepender())
                .addLast(new ProtobufEncoder())
                .addLast(new TransClassNameServerHandler());


    }

    private void initChannelKqueue(KQueueSocketChannel socketChannel){

    }

    private void initChannelEpoll(EpollSocketChannel socketChannel){

    }

    private void initChannelNio(NioSocketChannel socketChannel){

    }

}
