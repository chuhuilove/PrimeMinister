package com.chuhui.primeminister.core.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;


/**
 * NetworkReader
 *
 * @author: cyzi
 * @Date: 2019/9/5 0005
 * @Description:
 */
@Slf4j
public class NetworkReader extends SimpleChannelInboundHandler<ByteBuf> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        System.err.print(msg.toString(CharsetUtil.UTF_8));
    }

    //

}
