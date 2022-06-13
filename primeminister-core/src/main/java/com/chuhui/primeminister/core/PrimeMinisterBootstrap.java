package com.chuhui.primeminister.core;

import com.chuhui.primeminister.core.network.StartNetworkEvent;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * core
 *
 * @author: cyzi
 * @Date: 6/4/22
 * @Description:
 */
public class PrimeMinisterBootstrap {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(BootstrapConfig.class, args);
        applicationContext.publishEvent(new StartNetworkEvent(applicationContext));
    }

}
