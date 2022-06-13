package com.chuhui.primeminister.core.network;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * NettyConfiguration
 *
 * @author: cyzi
 * @Date: 6/4/22
 * @Description:
 */
@Component
@ConfigurationProperties(prefix = "primeminister.core.netty")
@Data
public class NettyConfiguration {

    private Integer port;
    private String host;

}
