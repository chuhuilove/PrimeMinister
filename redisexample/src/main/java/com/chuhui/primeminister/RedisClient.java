package com.chuhui.primeminister;

import redis.clients.jedis.Jedis;

import java.util.UUID;
import java.util.stream.IntStream;

/**
 * RedisClient
 *
 * @author: 纯阳子
 * @Date: 2019/7/4 0004
 * @Description:TODO
 */
public class RedisClient {


    public static void main(String[] args) {

        Jedis jedis = new Jedis("127.0.0.1",6379);

        // insert 20
        IntStream.rangeClosed(1,20).forEach(e->jedis.set(String.valueOf(e),UUID.randomUUID().toString()));







    }



}
