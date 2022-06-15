package com.chuhui.primeminister.core;

import com.chuhui.primeminister.core.network.NettyConfiguration;
import com.chuhui.primeminister.core.network.PrimeMinisterNetworkServer;
import com.chuhui.primeminister.core.network.zookeeper.ServerContext;
import com.chuhui.primeminister.core.network.zookeeper.ServerContextFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Priority;
import javax.annotation.Resource;

/**
 * PrimeMinisterServer
 *
 * @author: cyzi
 * @Date: 6/14/22
 * @Description:
 */
@Service
public class PrimeMinisterServer {

    //@Resource
    protected ServerContextFactory serverContextFactory;

    //  @Resource
    protected ServerContextFactory secureServerContextFactory;

    public void removeCnxn(ServerContext cnxn) {
//        zkDb.removeCnxn(cnxn);
    }

    @Resource
    private NettyConfiguration nettyConfiguration;

    private PrimeMinisterNetworkServer networkServer;

    @PostConstruct
    private void initPrimeMinisterServer() {
        networkServer = new PrimeMinisterNetworkServer(nettyConfiguration);
    }

    public PrimeMinisterNetworkServer getNetworkServer() {
        return networkServer;
    }
}
