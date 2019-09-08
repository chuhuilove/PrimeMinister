package com.chuhui.primeminister.customerredisclient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Connection
 * <p>
 * //TODO description
 *
 * @author: 纯阳子
 * @date: 2019/9/8
 */
public class Connection {

    private Socket socket;

    private String host;
    private int port;
    private int conTime;

    private OutputStream outputStream;
    private InputStream inputStream;


    public Connection(String host, int port) {
        this(host, port, 0);
    }

    public Connection(String host, int port, int conTime) {
        this.host = host;
        this.port = port;
        this.conTime = conTime;
    }

    public void connection() {

        try {
            socket = new Socket(host, port);
            socket.setSoTimeout(conTime);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Connection sendComand(Protocol.Command command, byte[] args) {
        connection();

        Protocol.sendCommand(outputStream, command, args);

        return this;
    }

    public String getStatusCocdereply() {

        byte[] bytes = new byte[1024];
        try {
            inputStream.read(bytes);
            return new String(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
