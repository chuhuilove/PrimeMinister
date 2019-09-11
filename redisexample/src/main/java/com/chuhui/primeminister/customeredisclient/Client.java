package com.chuhui.primeminister.customeredisclient;

/**
 * Client
 * <p>
 * //TODO description
 *
 * @author: 纯阳子
 * @date: 2019/9/8
 */
public class Client {

    private Connection connection;


    public Client(String host, int port) {
        connection = new Connection(host, port);
    }

    public String set(String key, String value) {

        connection.sendComand(Protocol.Command.SET, key.getBytes(),value.getBytes());

        return null;
    }

    public String get(String key) {
        return null;
    }

    public static void main(String[] args) {


        Client client = new Client("118.24.141.172", 7002);
        client.set("cyzi", "2345");


    }


}
