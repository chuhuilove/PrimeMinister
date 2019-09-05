package com.chuhui.primeminister;

import com.chuhui.primeminister.client.DefaultNettyClient;
import com.chuhui.primeminister.client.NettyClientInterface;
import org.junit.Before;
import org.junit.Test;

/**
 * NettyServerTest
 *
 * @author: cyzi
 * @Date: 2019/9/2 0002
 * @Description:TODO
 */
public class NettyServerClientTest {

    private NettyClientInterface client;


    @Before
    public void initliazer() {
        client=new DefaultNettyClient("127.0.0.1",13225);
    }


    @Test
    public void connectToServer() {
        client.connection();

    }


}
