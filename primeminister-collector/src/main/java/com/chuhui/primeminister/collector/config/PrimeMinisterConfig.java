package com.chuhui.primeminister.collector.config;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;
import java.util.Set;

/**
 * PrimeMinisterConfig
 *
 * @author: cyzi
 * @Date: 6/9/22
 * @Description:
 */
final public class PrimeMinisterConfig {

    /**
     * 收集器配置文件的名称
     */
    public final static String PRIME_MINISTER_CONFIG_FILE_NAME = "prime-minister-config.yml";


    private ServerConfig serverConfig;
    private FilterConfig filterConfig;

    private static PrimeMinisterConfig configInstance=null;


    public static PrimeMinisterConfig getConfigInstance() {
        // 双检锁,先不用考虑
        return configInstance;
    }

    private PrimeMinisterConfig() {
        Yaml yaml = new Yaml();

        InputStream textFileStream = ClassLoader.getSystemResourceAsStream(PRIME_MINISTER_CONFIG_FILE_NAME);
        if(Objects.isNull(textFileStream)){
            throw new NullPointerException("没有配置文件!!!");
        }
    }

    public ServerConfig getServerConfig() {
        return serverConfig;
    }

    public FilterConfig getFilterConfig() {
        return filterConfig;
    }
}
