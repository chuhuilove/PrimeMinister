package com.chuhui.primeminister.core;

import com.chuhui.primeminister.core.network.PrimeMinisterNetworkServer;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * PrimeMinisterBootstrap
 *
 * @author: cyzi
 * @Date: 6/4/22
 * @Description:
 */
public class PrimeMinisterBootstrap {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(BootstrapConfig.class, args);

        PrimeMinisterServer mainServer = applicationContext.getBean(PrimeMinisterServer.class);
        PrimeMinisterNetworkServer mainNetworkServer = mainServer.getNetworkServer();
        // 启动网络监听服务,阻塞main线程
        mainNetworkServer.startUp();
    }

}
