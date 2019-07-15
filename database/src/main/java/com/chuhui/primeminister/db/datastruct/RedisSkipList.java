package com.chuhui.primeminister.db.datastruct;

/**
 * SkipList
 *
 * @author: 纯阳子
 * @Date: 2019/6/27
 * @Description: 跳跃表实现 redis中跳跃表的java版
 */
public class RedisSkipList {


    static final int ZSKIPLIST_MAXLEVEL = 64;

    public ZSkipListNode zslCreateNode(int level, double score, String ele) {
        ZSkipListNode listNode = new ZSkipListNode();
        listNode.score = score;
        listNode.ele = ele;
        return listNode;

    }






    static class ZSkipListNode {

        private String ele;
        private double score;
        private ZSkipListNode backward;
        private ZSkipListLevel[] level;
    }

    static class ZSkipListLevel {
        private ZSkipListNode forward;
        private long span;
    }


    private ZSkipListNode header;
    private ZSkipListNode tail;
    private long length;
    private int level;
}
