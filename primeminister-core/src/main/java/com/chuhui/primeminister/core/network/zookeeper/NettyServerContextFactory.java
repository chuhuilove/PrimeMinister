package com.chuhui.primeminister.core.network.zookeeper;

import com.chuhui.primeminister.common.network.NettyUtils;
import com.chuhui.primeminister.core.PrimeMinisterServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.DefaultEventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * NettyServerContextFactory
 *
 * @author: cyzi
 * @Date: 6/14/22
 * @Description:
 */
@Service
public class NettyServerContextFactory  extends ServerContextFactory{


    private   ServerBootstrap bootstrap;
    private Channel parentChannel;
    private final ChannelGroup allChannels =
            new DefaultChannelGroup("zkServerCnxns", new DefaultEventExecutor());
    // Access to ipMap or to any Set contained in the map needs to be
    // protected with synchronized (ipMap) { ... }
    private final Map<InetAddress, Set<NettyServerContext>> ipMap = new HashMap<>();
    private InetSocketAddress localAddress;
    private int maxClientCnxns = 60;


    private static final AttributeKey<NettyServerContext> CONNECTION_ATTRIBUTE =
            AttributeKey.valueOf("NettyServerContext");

    private static final AtomicReference<ByteBufAllocator> TEST_ALLOCATOR =
            new AtomicReference<>(null);




    @ChannelHandler.Sharable
    class ContextChannelHandler extends ChannelDuplexHandler {

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            /**
             * 每个客户端上线,都会调用到这个函数
             */
            NettyServerContext cnxn = new NettyServerContext(ctx.channel(),pmServer, NettyServerContextFactory.this);
            ctx.channel().attr(CONNECTION_ATTRIBUTE).set(cnxn);

            if (secure) {
                SslHandler sslHandler = ctx.pipeline().get(SslHandler.class);
                Future<Channel> handshakeFuture = sslHandler.handshakeFuture();
               // handshakeFuture.addListener(new CertificateVerifier(sslHandler, cnxn));
            } else {
                allChannels.add(ctx.channel());
                addCnxn(cnxn);
            }
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            /**
             * 每一个客户端掉线,都会调用到这个函数
             */
//            if (LOG.isTraceEnabled()) {
//                LOG.trace("cyzi:Channel inactive {}", ctx.channel());
//            }
            allChannels.remove(ctx.channel());
            NettyServerContext cnxn = ctx.channel().attr(CONNECTION_ATTRIBUTE).getAndSet(null);
            if (cnxn != null) {
//                if (LOG.isTraceEnabled()) {
//                    LOG.trace("Channel inactive caused close {}", cnxn);
//                }
                cnxn.close();
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//            LOG.warn("Exception caught", cause);
            NettyServerContext cnxn = ctx.channel().attr(CONNECTION_ATTRIBUTE).getAndSet(null);
            if (cnxn != null) {
//                if (LOG.isDebugEnabled()) {
//                    LOG.debug("Closing {}", cnxn);
//                }
                cnxn.close();
            }
        }

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            try {
                if (evt == NettyServerContext.AutoReadEvent.ENABLE) {
                    //LOG.debug("Received AutoReadEvent.ENABLE");
                    NettyServerContext cnxn = ctx.channel().attr(CONNECTION_ATTRIBUTE).get();
                    // TODO(ivmaykov): Not sure if cnxn can be null here. It becomes null if channelInactive()
                    // or exceptionCaught() trigger, but it's unclear to me if userEventTriggered() can run
                    // after either of those. Check for null just to be safe ...
                    if (cnxn != null) {
                        cnxn.processQueuedBuffer();
                    }
                    ctx.channel().config().setAutoRead(true);
                } else if (evt == NettyServerContext.AutoReadEvent.DISABLE) {
                   // LOG.debug("Received AutoReadEvent.DISABLE");
                    ctx.channel().config().setAutoRead(false);
                }
            } finally {
                ReferenceCountUtil.release(evt);
            }
        }


        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            /**
             * 每次消息过来,都要走这个地方
             */
            try {
//                if (LOG.isTraceEnabled()) {
//                    LOG.trace("cyzi:message received called {}", msg);
//                }
                try {
//                    if (LOG.isDebugEnabled()) {
//                        LOG.debug("New message {} from {}", msg, ctx.channel());
//                    }
                    NettyServerContext cnxn = ctx.channel().attr(CONNECTION_ATTRIBUTE).get();
                    if (cnxn == null) {
                       // LOG.error("channelRead() on a closed or closing NettyServerCnxn");
                    } else {
                        cnxn.processMessage((ByteBuf) msg);
                    }
                } catch (Exception ex) {
                    //LOG.error("Unexpected exception in receive", ex);
                    throw ex;
                }
            } finally {
                ReferenceCountUtil.release(msg);
            }
        }

        // Use a single listener instance to reduce GC
        // Note: this listener is only added when LOG.isTraceEnabled() is true,
        // so it should not do any work other than trace logging.
        private final GenericFutureListener<Future<Void>> onWriteCompletedTracer = (f) -> {
          //  LOG.trace("write {}", f.isSuccess() ? "complete" : "failed");
        };

        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
//            if (LOG.isTraceEnabled()) {
//                promise.addListener(onWriteCompletedTracer);
//            }
            super.write(ctx, msg, promise);
        }


//        private final class CertificateVerifier implements GenericFutureListener<Future<Channel>> {
//            private final SslHandler sslHandler;
//            private final NettyServerContext cnxn;
//
//            CertificateVerifier(SslHandler sslHandler, NettyServerCnxn cnxn) {
//                this.sslHandler = sslHandler;
//                this.cnxn = cnxn;
//            }
//
//            /**
//             * Only allow the connection to stay open if certificate passes auth
//             */
//            @Override
//            public void operationComplete(Future<Channel> future) throws SSLPeerUnverifiedException {
//                if (future.isSuccess()) {
//                    if (LOG.isDebugEnabled()) {
//                        LOG.debug("Successful handshake with session 0x{}",
//                                Long.toHexString(cnxn.getSessionId()));
//                    }
//                    SSLEngine eng = sslHandler.engine();
//                    SSLSession session = eng.getSession();
//                    cnxn.setClientCertificateChain(session.getPeerCertificates());
//
//                    String authProviderProp
//                            = System.getProperty(x509Util.getSslAuthProviderProperty(), "x509");
//
//                    X509AuthenticationProvider authProvider =
//                            (X509AuthenticationProvider)
//                                    ProviderRegistry.getProvider(authProviderProp);
//
//                    if (authProvider == null) {
//                        LOG.error("Auth provider not found: {}", authProviderProp);
//                        cnxn.close();
//                        return;
//                    }
//
//                    if (KeeperException.Code.OK !=
//                            authProvider.handleAuthentication(cnxn, null)) {
//                        LOG.error("Authentication failed for session 0x{}",
//                                Long.toHexString(cnxn.getSessionId()));
//                        cnxn.close();
//                        return;
//                    }
//
//                    final Channel futureChannel = future.getNow();
//                    allChannels.add(Objects.requireNonNull(futureChannel));
//                    addCnxn(cnxn);
//                } else {
//                    LOG.error("Unsuccessful handshake with session 0x{}",
//                            Long.toHexString(cnxn.getSessionId()));
//                    cnxn.close();
//                }
//            }
//        }


    }

    ContextChannelHandler channelHandler = new ContextChannelHandler();


    private ServerBootstrap configureBootstrapAllocator(ServerBootstrap bootstrap) {
        ByteBufAllocator testAllocator = TEST_ALLOCATOR.get();
        if (testAllocator != null) {
            return bootstrap
                    .option(ChannelOption.ALLOCATOR, testAllocator)
                    .childOption(ChannelOption.ALLOCATOR, testAllocator);
        } else {
            return bootstrap;
        }
    }



    NettyServerContextFactory() {

        // 服务端的机器上有多少个可用地址,就开多少个线程
        int reachableCount = NettyUtils.getClientReachableLocalInetAddressCount();

       // LOG.info("this machine client reachable local InetAddress count:{}", reachableCount);

        EventLoopGroup bossGroup = NettyUtils.newNioOrEpollEventLoopGroup(reachableCount);
        EventLoopGroup workerGroup = NettyUtils.newNioOrEpollEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(bossGroup, workerGroup)
                .channel(NettyUtils.nioOrEpollServerSocketChannel())
                // parent channel options
                .option(ChannelOption.SO_REUSEADDR, true)
                // child channels options
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_LINGER, -1)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
//                        if (secure) {
//                            initSSL(pipeline);
//                        }
                        pipeline.addLast("servercnxnfactory", channelHandler);
                    }
                });


        this.bootstrap = configureBootstrapAllocator(bootstrap);
        this.bootstrap.validate();
    }




    @Override
    public int getLocalPort() {
        return localAddress.getPort();
    }

    @Override
    public Iterable<ServerContext> getConnections() {
        return null;
    }

    @Override
    public boolean closeSession(long sessionId) {
        return false;
    }

    @Override
    public void configure(InetSocketAddress addr, int maxcc, boolean secure) throws IOException {

    }

    @Override
    public void reconfigure(InetSocketAddress addr) {

    }

    @Override
    public int getMaxClientCnxnsPerHost() {
        return 0;
    }

    @Override
    public void setMaxClientCnxnsPerHost(int max) {

    }

    @Override
    public void startup(PrimeMinisterServer zkServer, boolean startServer) throws IOException, InterruptedException {



        // 启动Netty服务
        start();
        // 设置zookeeper服务...
//        setZooKeeperServer(zks);
//        if (startServer) {
//            zks.startdata();
//            zks.startup();
//        }
    }

    @Override
    public void join() throws InterruptedException {

    }

    @Override
    public void shutdown() {

    }

    @Override
    public void start() {

      //  LOG.info("binding to port {}", localAddress);
        parentChannel = bootstrap.bind(localAddress).syncUninterruptibly().channel();
        // Port changes after bind() if the original port was 0, update
        // localAddress to get the real port.
        /**
         * 如果原始端口为0,则bind()之后的端口会更改,
         * 则更新localAddress以获取实际端口.
         */
        localAddress = (InetSocketAddress) parentChannel.localAddress();
      //  LOG.info("bound to port " + getLocalPort());

    }

    @Override
    public void closeAll() {

    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return null;
    }

    @Override
    public void resetAllConnectionStats() {

    }

    void removeCnxnFromIpMap(NettyServerContext cnxn, InetAddress remoteAddress) {
        synchronized (ipMap) {
            Set<NettyServerContext> s = ipMap.get(remoteAddress);
            if (s != null) {
                s.remove(cnxn);
                if (s.isEmpty()) {
                    ipMap.remove(remoteAddress);
                }
                return;
            }
        }
        // Fallthrough and log errors outside the synchronized block
//        LOG.error(
//                "Unexpected null set for remote address {} when removing cnxn {}",
//                remoteAddress,
//                cnxn);
    }

    @Override
    public Iterable<Map<String, Object>> getAllConnectionInfo(boolean brief) {
        return null;
    }

    private void addCnxn(NettyServerContext cnxn) {
        cnxns.add(cnxn);
        synchronized (ipMap) {
            InetAddress addr =
                    ((InetSocketAddress) cnxn.getChannel().remoteAddress()).getAddress();
            Set<NettyServerContext> s = ipMap.get(addr);
            if (s == null) {
                s = new HashSet<>();
                ipMap.put(addr, s);
            }
            s.add(cnxn);
        }
    }
}
