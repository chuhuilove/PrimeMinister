package com.chuhui.primeminister;

import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.UUID;

/**
 * RedisClient
 *
 * @author: 纯阳子
 * @Date: 2019/7/4 0004
 * @Description:TODO
 */
public class RedisClient {


    public static void main(String[] args) {


        Jedis jedis=new Jedis("xxx.xx",7002);

        for (int i = 101; i < 200; i++) {

            jedis.set("cyzi"+(i+1),UUID.randomUUID().toString());

        }


    }



}
