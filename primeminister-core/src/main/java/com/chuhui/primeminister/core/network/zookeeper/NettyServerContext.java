package com.chuhui.primeminister.core.network.zookeeper;

import com.chuhui.primeminister.core.PrimeMinisterServer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.security.cert.Certificate;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * NettyServerContext
 *
 * @author: cyzi
 * @Date: 6/14/22
 * @Description:
 */
public class NettyServerContext extends ServerContext {

    private final Channel channel;
    private CompositeByteBuf queuedBuffer;
    private final AtomicBoolean throttled = new AtomicBoolean(false);
    private ByteBuffer bb;
    private final ByteBuffer bbLen = ByteBuffer.allocate(4);
    private long sessionId;
    private int sessionTimeout;
    private AtomicLong outstandingCount = new AtomicLong();
    private Certificate[] clientChain;
    private volatile boolean closingChannel;




    private volatile PrimeMinisterServer pmServer;

    private final NettyServerContextFactory factory;
    private boolean initialized;

    public NettyServerContext(Channel channel, PrimeMinisterServer pmServer, NettyServerContextFactory nettyServerContextFactory) {
        this.channel = channel;
        this.closingChannel = false;
        this.pmServer = pmServer;
        this.factory = nettyServerContextFactory;
    }
    Channel getChannel() {
        return channel;
    }



    public void close() {
        closingChannel = true;

//        if (LOG.isDebugEnabled()) {
//            LOG.debug("close called for sessionid:0x{}",
//                    Long.toHexString(sessionId));
//        }

        // ZOOKEEPER-2743:
        // Always unregister connection upon close to prevent
        // connection bean leak under certain race conditions.
        factory.unregisterConnection(this);

        // if this is not in cnxns then it's already closed
        if (!factory.cnxns.remove(this)) {
//            if (LOG.isDebugEnabled()) {
//                LOG.debug("cnxns size:{}", factory.cnxns.size());
//            }
            return;
        }
//        if (LOG.isDebugEnabled()) {
//            LOG.debug("close in progress for sessionid:0x{}",
//                    Long.toHexString(sessionId));
//        }

        factory.removeCnxnFromIpMap(
                this,
                ((InetSocketAddress)channel.remoteAddress()).getAddress());

        if (pmServer != null) {
            pmServer.removeCnxn(this);
        }

        if (channel.isOpen()) {
            // Since we don't check on the futures created by write calls to the channel complete we need to make sure
            // that all writes have been completed before closing the channel or we risk data loss
            // See: http://lists.jboss.org/pipermail/netty-users/2009-August/001122.html
            channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) {
                    future.channel().close().addListener(f -> releaseQueuedBuffer());
                }
            });
        } else {
            channel.eventLoop().execute(this::releaseQueuedBuffer);
        }
    }
    private void releaseQueuedBuffer() {
        checkIsInEventLoop("releaseQueuedBuffer");
        if (queuedBuffer != null) {
            queuedBuffer.release();
            queuedBuffer = null;
        }
    }

    private void checkIsInEventLoop(String callerMethodName) {
        if (!channel.eventLoop().inEventLoop()) {
            throw new IllegalStateException(
                    callerMethodName + "() called from non-EventLoop thread");
        }
    }


    enum AutoReadEvent {
        DISABLE,
        ENABLE
    }

    private void receiveMessage(ByteBuf message) {
        checkIsInEventLoop("receiveMessage");
       // try {
            while(message.isReadable() && !throttled.get()) {
                if (bb != null) {
//                    if (LOG.isTraceEnabled()) {
//                        LOG.trace("message readable {} bb len {} {}",
//                                message.readableBytes(),
//                                bb.remaining(),
//                                bb);
//                        ByteBuffer dat = bb.duplicate();
//                        dat.flip();
//                        LOG.trace("0x{} bb {}",
//                                Long.toHexString(sessionId),
//                                ByteBufUtil.hexDump(Unpooled.wrappedBuffer(dat)));
//                    }

                    if (bb.remaining() > message.readableBytes()) {
                        int newLimit = bb.position() + message.readableBytes();
                        bb.limit(newLimit);
                    }
                    message.readBytes(bb);
                    bb.limit(bb.capacity());

//                    if (LOG.isTraceEnabled()) {
//                        LOG.trace("after readBytes message readable {} bb len {} {}",
//                                message.readableBytes(),
//                                bb.remaining(),
//                                bb);
//                        ByteBuffer dat = bb.duplicate();
//                        dat.flip();
//                        LOG.trace("after readbytes 0x{} bb {}",
//                                Long.toHexString(sessionId),
//                                ByteBufUtil.hexDump(Unpooled.wrappedBuffer(dat)));
//                    }
                    if (bb.remaining() == 0) {
                        packetReceived();
                        bb.flip();

//                        ZooKeeperServer zks = this.zkServer;
//                        if (zks == null || !zks.isRunning()) {
//                            throw new IOException("ZK down");
//                        }
//                        if (initialized) {
//                            // TODO: if zks.processPacket() is changed to take a ByteBuffer[],
//                            // we could implement zero-copy queueing.
//                            zks.processPacket(this, bb);
//
//                            if (zks.shouldThrottle(outstandingCount.incrementAndGet())) {
//                                disableRecvNoWait();
//                            }
//                        } else {
//                            if (LOG.isDebugEnabled()) {
//                                LOG.debug("got conn req request from {}",
//                                        getRemoteSocketAddress());
//                            }
//                            zks.processConnectRequest(this, bb);
//                            initialized = true;
//                        }
//                        bb = null;
                    }
                } else {
//                    if (LOG.isTraceEnabled()) {
//                        LOG.trace("message readable {} bblenrem {}",
//                                message.readableBytes(),
//                                bbLen.remaining());
//                        ByteBuffer dat = bbLen.duplicate();
//                        dat.flip();
//                        LOG.trace("0x{} bbLen {}",
//                                Long.toHexString(sessionId),
//                                ByteBufUtil.hexDump(Unpooled.wrappedBuffer(dat)));
//                    }

                    if (message.readableBytes() < bbLen.remaining()) {
                        bbLen.limit(bbLen.position() + message.readableBytes());
                    }
                    message.readBytes(bbLen);
                    bbLen.limit(bbLen.capacity());
                    if (bbLen.remaining() == 0) {
                        bbLen.flip();

//                        if (LOG.isTraceEnabled()) {
//                            LOG.trace("0x{} bbLen {}",
//                                    Long.toHexString(sessionId),
//                                    ByteBufUtil.hexDump(Unpooled.wrappedBuffer(bbLen)));
//                        }
                        int len = bbLen.getInt();
//                        if (LOG.isTraceEnabled()) {
//                            LOG.trace("0x{} bbLen len is {}",
//                                    Long.toHexString(sessionId),
//                                    len);
//                        }

                        bbLen.clear();
//                        if (!initialized) {
//                            if (checkFourLetterWord(channel, message, len)) {
//                                return;
//                            }
//                        }
//                        if (len < 0 || len > BinaryInputArchive.maxBuffer) {
//                            throw new IOException("Len error " + len);
//                        }
                        bb = ByteBuffer.allocate(len);
                    }
                }
            }
//        } catch(IOException e) {
//           // LOG.warn("Closing connection to " + getRemoteSocketAddress(), e);
//            close();
//        }
    }

    public InetSocketAddress getRemoteSocketAddress() {
        return (InetSocketAddress)channel.remoteAddress();
    }

    void processQueuedBuffer() {
        checkIsInEventLoop("processQueuedBuffer");
        if (queuedBuffer != null) {
//            if (LOG.isTraceEnabled()) {
//                LOG.trace("processing queue 0x{} queuedBuffer {}",
//                        Long.toHexString(sessionId),
//                        ByteBufUtil.hexDump(queuedBuffer));
//            }
            receiveMessage(queuedBuffer);
            if (closingChannel) {
                // close() could have been called if receiveMessage() failed
               // LOG.debug("Processed queue - channel closed, dropping remaining bytes");
            } else if (!queuedBuffer.isReadable()) {
              //  LOG.debug("Processed queue - no bytes remaining");
                releaseQueuedBuffer();
            } else {
               // LOG.debug("Processed queue - bytes remaining");
                // Try to reduce memory consumption by freeing up buffer space
                // which is no longer needed.
                queuedBuffer.discardReadComponents();
            }
        } else {
           // LOG.debug("queue empty");
        }
    }

    protected void packetReceived() {
        incrPacketsReceived();
//        ServerStats serverStats = serverStats();
//        if (serverStats != null) {
//            serverStats().incrementPacketsReceived();
//        }
    }


    void processMessage(ByteBuf buf) {

//
//        checkIsInEventLoop("processMessage");
//        if (LOG.isDebugEnabled()) {
//            LOG.debug("0x{} queuedBuffer: {}",
//                    Long.toHexString(sessionId),
//                    queuedBuffer);
//        }
//
//        if (LOG.isTraceEnabled()) {
//            LOG.trace("0x{} buf {}",
//                    Long.toHexString(sessionId),
//                    ByteBufUtil.hexDump(buf));
//        }
//
//        if (throttled.get()) {
//            LOG.debug("Received message while throttled");
//            // we are throttled, so we need to queue
//            if (queuedBuffer == null) {
//                LOG.debug("allocating queue");
//                queuedBuffer = channel.alloc().compositeBuffer();
//            }
//            appendToQueuedBuffer(buf.retainedDuplicate());
//            if (LOG.isTraceEnabled()) {
//                LOG.trace("0x{} queuedBuffer {}",
//                        Long.toHexString(sessionId),
//                        ByteBufUtil.hexDump(queuedBuffer));
//            }
//        } else {
//            LOG.debug("not throttled");
//            if (queuedBuffer != null) {
//                appendToQueuedBuffer(buf.retainedDuplicate());
//                processQueuedBuffer();
//            } else {
//                receiveMessage(buf);
//                // Have to check !closingChannel, because an error in
//                // receiveMessage() could have led to close() being called.
//                if (!closingChannel && buf.isReadable()) {
//                    if (LOG.isTraceEnabled()) {
//                        LOG.trace("Before copy {}", buf);
//                    }
//                    if (queuedBuffer == null) {
//                        queuedBuffer = channel.alloc().compositeBuffer();
//                    }
//                    appendToQueuedBuffer(buf.retainedSlice(buf.readerIndex(), buf.readableBytes()));
//                    if (LOG.isTraceEnabled()) {
//                        LOG.trace("Copy is {}", queuedBuffer);
//                        LOG.trace("0x{} queuedBuffer {}",
//                                Long.toHexString(sessionId),
//                                ByteBufUtil.hexDump(queuedBuffer));
//                    }
//                }
//            }
//        }



    }



}
