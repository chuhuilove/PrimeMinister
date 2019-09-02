package com.chuhui.primeminister.server;

import org.omg.PortableInterceptor.INACTIVE;
import org.omg.PortableServer.SERVANT_RETENTION_POLICY_ID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * ServerConfig
 * 解析配置文件
 * //TODO 先期先集成在resources文件夹下,先跑起来再说
 *
 * @author: cyzi
 * @Date: 2019/9/2 0002
 * @Description:
 */
public class ServerConfig {
    private static final Logger LOG =
            LoggerFactory.getLogger(ServerConfig.class);

    private static volatile ServerConfig instance;

    public static ServerConfig getInstance() {
        if (instance == null) {
            instance = new ServerConfig();
        }
        return instance;
    }

    private ServerConfig() {

    }

    private DBServerConfigureModel configureModel;


    public void parse(String path) {

        try {
            FileInputStream fileInputStream = new FileInputStream(path);
            parse(fileInputStream);
        } catch (FileNotFoundException e) {
            LOG.error("file:{} not found.server will exit.", path, e);
            System.exit(1);
        }

    }

    public void parse(InputStream inputStream) {
        Yaml yaml = new Yaml();
        configureModel = yaml.loadAs(inputStream, DBServerConfigureModel.class);
    }

    public DBServerConfigureModel getConfigureModel() {
        return configureModel;
    }

}
