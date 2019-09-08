package com.chuhui.primeminister;

import com.chuhui.primeminister.customerredisclient.Connection;
import com.chuhui.primeminister.customerredisclient.Protocol;

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


    public Client(String host,int port){
        connection=new Connection(host,port);
    }

    public String set(String key,String value){

        connection.sendComand(Protocol.Command.SET,key.getBytes());

        return null;
    }

    public String  get(String key){
        return null;
    }


}
