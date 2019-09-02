package com.chuhui.primeminister.server;

/**
 * DBServerDataModel
 *
 * @author: cyzi
 * @Date: 2019/9/2 0002
 * @Description:TODO
 */
public class DBServerConfigureModel {

    private ServerModel server;


    public ServerModel getServer() {
        return server;
    }

    public void setServer(ServerModel server) {
        this.server = server;
    }

    public static class ServerModel {
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

}
