package com.chuhui.primeminister.collector.network;

import com.chuhui.primeminister.common.network.NettyUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioChannelOption;
import io.netty.handler.ssl.SslHandler;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * NetworkClient
 *
 * @author: cyzi
 * @Date: 6/13/22
 * @Description:
 */
public class ClientSocketZookeeper {

    private final EventLoopGroup eventLoopGroup;
    private Channel channel;
    private CountDownLatch firstConnect;
    private ChannelFuture connectFuture;
    private final Lock connectLock = new ReentrantLock();
    private final AtomicBoolean disconnected = new AtomicBoolean();
    private final AtomicBoolean needSasl = new AtomicBoolean();
    private final Semaphore waitSasl = new Semaphore(0);

    private static final AtomicReference<ByteBufAllocator> TEST_ALLOCATOR =
            new AtomicReference<>(null);



    public ClientSocketZookeeper(){
        eventLoopGroup= NettyUtils.newNioOrEpollEventLoopGroup(1);

    }


    boolean isConnected() {
        // Assuming that isConnected() is only used to initiate connection,
        // not used by some other connection status judgement.
        /**
         * 假设isConnected()仅用于初始化连接,而不是用于其他连接状态判断.
         */
        connectLock.lock();
        try {
            return channel != null || connectFuture != null;
        } finally {
            connectLock.unlock();
        }
    }

    void connect(InetSocketAddress addr) throws IOException {
        firstConnect = new CountDownLatch(1);

        /**
         * 读源码的时候,每一行代码都认为是理所当然的,这种思想是不对的.
         * 如果让我去设计者东西,我该怎么解决呢?
         * 比如,这里为什么要加锁呢?还有其他地方调用connect方法吗?
         * 加锁是为了保护谁?
         * 这里为什么调用firstConnect = new CountDownLatch(1);
         *
         */

        Bootstrap bootstrap = new Bootstrap()
                .group(eventLoopGroup)
                .channel(NettyUtils.nioOrEpollSocketChannel())
                .option(ChannelOption.SO_LINGER, -1)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ZKClientPipelineFactory(addr.getHostString(), addr.getPort()));

        bootstrap = configureBootstrapAllocator(bootstrap);
        bootstrap.validate();

        connectLock.lock();
        try {
            // 这一行code,是连接到服务端,没错了....
            connectFuture = bootstrap.connect(addr);
            //添加监听器
            connectFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {

//                    logStackInfo("在org.apache.zookeeper.ClientCnxnSocketNetty.connect函数中");

                    // this lock guarantees that channel won't be assigned after cleanup().
                    // 此锁确保在cleanup()之后不会分配通道.
                    boolean connected = false;
                   // LOG.info("connectLock.lock");

                  //  LOG.info("connectLock into");
                    try {
                        connectLock.lock();
                        if (!channelFuture.isSuccess()) {
                           // LOG.info("cyzi----->future isn't success, cause:", channelFuture.cause());
                            return;
                        } else if (connectFuture == null) {
                         //   LOG.info("connect attempt cancelled");
                            // If the connect attempt was cancelled but succeeded
                            // anyway, make sure to close the channel, otherwise
                            // we may leak a file descriptor.
                            channelFuture.channel().close();
                            return;
                        }
                        // setup channel, variables, connection, etc.
                        channel = channelFuture.channel();

                        disconnected.set(false);
//                        initialized = false;
//                        lenBuffer.clear();
//                        incomingBuffer = lenBuffer;
//
//                        sendThread.primeConnection();
//                        updateNow();
//                        updateLastSendAndHeard();

//                        if (sendThread.tunnelAuthInProgress()) {
//                            waitSasl.drainPermits();
//                            needSasl.set(true);
//                            sendPrimePacket();
//                        } else {
//                            needSasl.set(false);
//                        }
                        connected = true;
                    } finally {
                        connectFuture = null;
                        connectLock.unlock();
                        if (connected) {
                           // LOG.info("cyzi----->channel is connected: {}", channelFuture.channel());
                        }
                        // need to wake on connect success or failure to avoid
                        // timing out ClientCnxn.SendThread which may be
                        // blocked waiting for first connect in doTransport().
//                        wakeupCnxn();
                        firstConnect.countDown();
                    }
                }
            });
        } finally {
            connectLock.unlock();
        }
    }


    private class ZKClientPipelineFactory extends ChannelInitializer<SocketChannel> {
        private SSLContext sslContext = null;
        private SSLEngine sslEngine = null;
        private String host;
        private int port;

        public ZKClientPipelineFactory(String host, int port) {
            this.host = host;
            this.port = port;
        }

        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ChannelPipeline pipeline = ch.pipeline();
//            if (clientConfig.getBoolean(ZKClientConfig.SECURE_CLIENT)) {
//                initSSL(pipeline);
//            }
            pipeline.addLast("handler", new ZKClientHandler());
        }


    }

    private class ZKClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
        AtomicBoolean channelClosed = new AtomicBoolean(false);

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
         //   LOG.info("channel is disconnected: {}", ctx.channel());
            cleanup();
        }

        /**
         * netty handler has encountered problems. We are cleaning it up and tell outside to close
         * the channel/connection.
         */
        private void cleanup() {
            if (!channelClosed.compareAndSet(false, true)) {
                return;
            }
            disconnected.set(true);
            onClosing();
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
          //  updateNow();
            while (buf.isReadable()) {
//                if (incomingBuffer.remaining() > buf.readableBytes()) {
//                    int newLimit = incomingBuffer.position()
//                            + buf.readableBytes();
//                    incomingBuffer.limit(newLimit);
//                }
//                buf.readBytes(incomingBuffer);
//                incomingBuffer.limit(incomingBuffer.capacity());
//
//                if (!incomingBuffer.hasRemaining()) {
//                    incomingBuffer.flip();
//                    if (incomingBuffer == lenBuffer) {
//                        recvCount.getAndIncrement();
//                        readLength();
//                    } else if (!initialized) {
//                        readConnectResult();
//                        lenBuffer.clear();
//                        incomingBuffer = lenBuffer;
//                        initialized = true;
//                        updateLastHeard();
//                    } else {
//                        /**
//                         * 读取到来自服务端的数据后,
//                         * 调用readResponse进行解析
//                         */
//                        sendThread.readResponse(incomingBuffer);
//                        lenBuffer.clear();
//                        incomingBuffer = lenBuffer;
//                        updateLastHeard();
//                    }
//                }
            }
          //  wakeupCnxn();
            // Note: SimpleChannelInboundHandler releases the ByteBuf for us
            // so we don't need to do it.
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
          //  LOG.warn("Exception caught", cause);
            cleanup();
        }
    }

    private Bootstrap configureBootstrapAllocator(Bootstrap bootstrap) {
        ByteBufAllocator testAllocator = TEST_ALLOCATOR.get();
        if (testAllocator != null) {
            return bootstrap.option(ChannelOption.ALLOCATOR, testAllocator);
        } else {
            return bootstrap;
        }
    }


    void onClosing() {
        firstConnect.countDown();
//        wakeupCnxn();
//        LOG.info("channel is told closing");
    }



}
