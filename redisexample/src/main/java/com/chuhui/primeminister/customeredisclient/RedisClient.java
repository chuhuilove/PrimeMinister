package com.chuhui.primeminister.customeredisclient;

import redis.clients.jedis.Jedis;

/**
 * RedisClient
 *
 * @author: 纯阳子
 * @Date: 2019/7/4 0004
 * @Description:TODO
 */
public class RedisClient {


    public static void main(String[] args) {


        Jedis jedis=new Jedis("localhost", 7002);
//        Jedis jedis=new Jedis("118.24.141.172", 7002);

        jedis.set("cyzi" ,"1234");

    }



}
