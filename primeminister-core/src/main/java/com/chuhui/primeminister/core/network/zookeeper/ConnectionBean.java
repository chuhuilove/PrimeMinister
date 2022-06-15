package com.chuhui.primeminister.core.network.zookeeper;

import com.chuhui.primeminister.core.PrimeMinisterServer;

/**
 * ConnectionBean
 *
 * @author: cyzi
 * @Date: 6/14/22
 * @Description:
 */
public class ConnectionBean {


    private final ServerContext serverContext;
    private final PrimeMinisterServer pmServer;


    public ConnectionBean(ServerContext serverContext, PrimeMinisterServer pmServer) {

        this.serverContext = serverContext;
        this.pmServer = pmServer;
    }

}
