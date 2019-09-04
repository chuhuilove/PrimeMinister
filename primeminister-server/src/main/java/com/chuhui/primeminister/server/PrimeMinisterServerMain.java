package com.chuhui.primeminister.server;

import com.chuhui.primeminister.network.AbstractNettyRemoting;
import com.chuhui.primeminister.network.NettyRemotingServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

import static com.chuhui.primeminister.constant.Constants.DEFAULT_CONFIG_FILENAME;

/**
 * PrimeMinisterServerMain
 *
 * PrimeMinisterServer main class
 * @author: cyzi
 * @Date: 2019/9/2 0002
 * @Description:TODO
 */
public class PrimeMinisterServerMain {
    private static final Logger LOG =
            LoggerFactory.getLogger(PrimeMinisterServerMain.class);

    private AbstractNettyRemoting remotingServer;

    public static void main(String[] args) {

        PrimeMinisterServerMain serverMain = new PrimeMinisterServerMain();
        serverMain.initializeAndRun(args);
    }


    public void initializeAndRun(String[] args) {

        LOG.info("Starting server");

        // resolve configure file
        ServerConfig config = ServerConfig.getInstance();
        if (args != null && args.length == 1) {
            config.parse(args[0]);
        } else {
            InputStream stream = PrimeMinisterServerMain.class.getClassLoader().getResourceAsStream(DEFAULT_CONFIG_FILENAME);
            config.parse(stream);
        }

        // start listen server
        remotingServer = new NettyRemotingServer(config);
        remotingServer.start();
    }


}
