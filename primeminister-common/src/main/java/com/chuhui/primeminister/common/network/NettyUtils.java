package com.chuhui.primeminister.common.network;

import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.kqueue.KQueue;
import io.netty.channel.kqueue.KQueueEventLoopGroup;
import io.netty.channel.kqueue.KQueueServerSocketChannel;
import io.netty.channel.kqueue.KQueueSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Data;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * NettyUtils
 *
 * @author: cyzi
 * @Date: 6/14/22
 * @Description:
 */
public class NettyUtils {

    private NettyUtils() {
    }

    private static final int DEFAULT_INET_ADDRESS_COUNT = 1;

    public static EventLoopGroup newNioOrEpollEventLoopGroup() {
        if (Epoll.isAvailable()) {
            return new EpollEventLoopGroup();
        }

        if (KQueue.isAvailable()) {
            // for mac
            return new KQueueEventLoopGroup();
        }

        return new NioEventLoopGroup();
    }


    public static EventLoopGroup newNioOrEpollEventLoopGroup(int nThreads) {

        if (Epoll.isAvailable()) {
            return new EpollEventLoopGroup(nThreads);
        } else {
            return new NioEventLoopGroup(nThreads);
        }
    }


    public static EventLoopGroup platformEventLoopGroup(int nThreads) {
        if (Epoll.isAvailable()) {
            return new EpollEventLoopGroup(nThreads);
        }
        if (KQueue.isAvailable()) {
            // for mac
            return new KQueueEventLoopGroup(nThreads);
        }
        return new NioEventLoopGroup(nThreads);
    }


    public static Class<? extends SocketChannel> platformSocketChannel() {
        if (Epoll.isAvailable()) {
            return EpollSocketChannel.class;
        }
        if (KQueue.isAvailable()) {
            // for mac
            return KQueueSocketChannel.class;
        }

        return NioSocketChannel.class;
    }

    public static Class<? extends ServerSocketChannel> platformServerSocketChannel() {
        if (Epoll.isAvailable()) {
            return EpollServerSocketChannel.class;
        }

        if (KQueue.isAvailable()) {
            return KQueueServerSocketChannel.class;
        }

        return NioServerSocketChannel.class;
    }


    public static Class<? extends SocketChannel> nioOrEpollSocketChannel() {
        if (Epoll.isAvailable()) {
            return EpollSocketChannel.class;
        } else {
            return NioSocketChannel.class;
        }
    }


    public static int getClientReachableLocalInetAddressCount() {
        try {
            Set<InetAddress> validInetAddresses = new HashSet<>();
            Enumeration<NetworkInterface> allNetworkInterfaces = NetworkInterface.getNetworkInterfaces();
            for (NetworkInterface networkInterface : Collections.list(allNetworkInterfaces)) {
                for (InetAddress inetAddress : Collections.list(networkInterface.getInetAddresses())) {
                    if (inetAddress.isLinkLocalAddress()) {
//                        LOG.debug("Ignoring link-local InetAddress {}", inetAddress);
                        continue;
                    }
                    if (inetAddress.isMulticastAddress()) {
//                        LOG.debug("Ignoring multicast InetAddress {}", inetAddress);
                        continue;
                    }
                    if (inetAddress.isLoopbackAddress()) {
//                        LOG.debug("Ignoring loopback InetAddress {}", inetAddress);
                        continue;
                    }
                    validInetAddresses.add(inetAddress);
                }
            }
            //  LOG.debug("Detected {} local network addresses", validInetAddresses.size());
            //LOG.debug("Resolved local addresses are: {}", Arrays.toString(validInetAddresses.toArray()));
            return validInetAddresses.size() > 0 ? validInetAddresses.size() : DEFAULT_INET_ADDRESS_COUNT;
        } catch (SocketException ex) {
            // LOG.warn("Failed to list all network interfaces, assuming 1", ex);
            return DEFAULT_INET_ADDRESS_COUNT;
        }
    }


    public static Class<? extends ServerSocketChannel> nioOrEpollServerSocketChannel() {
        if (Epoll.isAvailable()) {
            return EpollServerSocketChannel.class;
        } else {
            return NioServerSocketChannel.class;
        }
    }
//
//    @Data
//    public static class PlatformHolder {
//
//
//        private final Class<? extends SocketChannel> socketChannel;
//        private final Class<? extends ServerSocketChannel> serverSocketChannel;
//
//
//        public PlatformHolder() {
//
//            socketChannel = platformSocketChannel();
//            serverSocketChannel = platformServerSocketChannel();
//        }
//        public PlatformHolder() {
//            this(0);
//        }
//    }


}
