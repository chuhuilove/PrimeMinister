package com.chuhui.primeminister.server;

import com.chuhui.primeminister.constant.enums.DB_COMMAND_ENUMS;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * PrimeMinisterServerHandler
 *
 * @author: cyzi
 * @Date: 2019/9/2 0002
 * @Description:TODO
 */
public class PrimeMinisterServerHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private static final Logger LOG =
            LoggerFactory.getLogger(PrimeMinisterServerMain.class);


    public PrimeMinisterServerHandler() {
        LOG.info("PrimeMinisterServerHandler has initialized");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        LOG.info("msg:{}",   msg.toString(CharsetUtil.UTF_8));

        //TODO 暂时先不考虑 google protobuf的问题..最重要的是将协议整明白
        //
        ctx.writeAndFlush(Unpooled.copiedBuffer(("<PMDB>from server data:"+ UUID.randomUUID().toString() +"</PMDB>").getBytes()));

        // 命令的存储,使用枚举
        DB_COMMAND_ENUMS set = DB_COMMAND_ENUMS.valueOf("set");


    }



    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        LOG.info("a client connected...");
        super.handlerAdded(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        LOG.warn("invoke function  handlerRemoved,client:{}", ctx.channel().remoteAddress());
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOG.warn("invoke exceptionCaught,client:{}", ctx.channel().remoteAddress(), cause);
        ctx.close();
    }
}
