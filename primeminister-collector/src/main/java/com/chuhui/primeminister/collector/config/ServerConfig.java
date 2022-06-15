package com.chuhui.primeminister.collector.config;

import com.chuhui.primeminister.collector.annotations.PrimeMinisterConfiguration;

/**
 * NetworkConfig
 *
 * @author: cyzi
 * @Date: 6/13/22
 * @Description:
 */

@PrimeMinisterConfiguration(prefix = "server")
public class ServerConfig {

    private String host;
    private int port;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
