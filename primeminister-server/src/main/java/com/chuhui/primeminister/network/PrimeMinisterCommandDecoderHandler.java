package com.chuhui.primeminister.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * PrimeMinisterDecoderHandler
 *
 * 解析请求体中的命令
 *
 * @author: cyzi
 * @Date: 2019/9/5 0005
 * @Description:TODO
 */
public class PrimeMinisterCommandDecoderHandler extends ByteToMessageDecoder {
    private static final Logger LOG =
            LoggerFactory.getLogger(PrimeMinisterCommandDecoderHandler.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {




    }
}
