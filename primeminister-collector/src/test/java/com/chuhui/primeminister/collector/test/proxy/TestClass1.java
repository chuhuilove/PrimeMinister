package com.chuhui.primeminister.collector.test.proxy;

import com.chuhui.primeminister.collector.annotations.PrimeMinisterTrace;

import java.util.UUID;

/**
 * TestClass1
 *
 * @author: cyzi
 * @Date: 6/6/22
 * @Description:
 */
@PrimeMinisterTrace
public class TestClass1 {


    public String getUUid(){
        return UUID.randomUUID().toString();
    }

    protected String getUUid2(){
        return getUUid();
    }

}
