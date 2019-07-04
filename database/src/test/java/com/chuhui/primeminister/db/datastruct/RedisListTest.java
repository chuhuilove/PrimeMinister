package com.chuhui.primeminister.db.datastruct;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.stream.IntStream;


/**
 * RedisListTest
 * {@link RedisList}的单元测试
 *
 * @author: 纯阳子
 * @Date: 2019/7/4 0004
 * @Description:TODO
 */
public class RedisListTest {


    private RedisList<Integer> redisList;

    @Before
    public void init() {
        redisList = new RedisList<>();
    }


    @Test
    public void listEmpty() {
        redisList.listEmpty();

    }

    @Test
    public void listAddNodeHead() {
        IntStream.rangeClosed(1, 20).forEach(e -> redisList.listAddNodeHead(e));
        assertEquals(20, redisList.size());

        listEmpty();

        assertEquals(0, redisList.size());
    }

    @Test
    public void listAddNodeTail() {
    }

    @Test
    public void listNext() {
    }

    @Test
    public void listRewind() {
    }

    @Test
    public void listRewindTail() {
    }

    @Test
    public void clone1() {
    }

    @Test
    public void listDelNode() {
    }

    @Test
    public void listSearchKey() {
    }

    @Test
    public void listIndex() {
    }

    @Test
    public void listRotate() {
    }

    @Test
    public void listInsertNode() {
    }

    @Test
    public void listInsertNode1() {
    }

    @Test
    public void listJoin() {
    }

    @Test
    public void listIndex1() {
    }

    @Test
    public void listDelNode1() {
    }

    @Test
    public void listGetIterator() {
    }

    @Test
    public void listAddNodeHead1() {
    }

    @Test
    public void listAddNodeTail1() {
    }

    @Test
    public void listRotate1() {
    }
}
