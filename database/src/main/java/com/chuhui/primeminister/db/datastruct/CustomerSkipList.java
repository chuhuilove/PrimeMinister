package com.chuhui.primeminister.db.datastruct;

import java.util.AbstractList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * SkipList
 *
 * @author: 纯阳子
 * @Date: 2019/7/15 0015
 * @Description:TODO
 */
public class CustomerSkipList<E extends Comparable<? super E>> implements Comparator<E> {
//https://blog.csdn.net/moakun/article/details/79997037
    /**
     * 当前最高层级
     */
    private int level;

    private ZSkipListNode<E> header, tail;

    int size;


    /**
     * 最高层
     */
    static final int ZSKIPLIST_MAXLEVEL = 64;
    static final float ZSKIPLIST_P = 0.25f;

    public CustomerSkipList() {

        level = 1;
        size = 0;
        header = createNode(0, null);
        for (int j = 0; j < ZSKIPLIST_MAXLEVEL; j++) {
            header.level[j].forward = null;
            header.level[j].span = 0;
        }
        header.backward = null;
        tail = null;
    }


    private ZSkipListNode<E> createNode(double score, E item) {


        ZSkipListNode<E> zn = new ZSkipListNode<>();


        zn.score = score;
        zn.ele = item;
        return zn;
    }


    public boolean add(E e) {


        // 层级
        //

        // 今天晚上的活,就是跳跃表了

        return true;
    }


    ZSkipListNode<E> zslInsert(double score, E ele) {
        ZSkipListNode<E>[] update = new ZSkipListNode[ZSKIPLIST_MAXLEVEL];

        ZSkipListNode<E> x;

        int[] rank = new int[ZSKIPLIST_MAXLEVEL];
        int i, level;

        x = header;
        for (i = this.level - 1; i >= 0; i--) {
            /* store rank that is crossed to reach the insert position */
            rank[i] = i == (this.level - 1) ? 0 : rank[i + 1];
            while (x.level[i].forward != null &&
                    (x.level[i].forward.score < score ||
                            (x.level[i].forward.score == score && compare(x.level[i].forward.ele, ele) < 0))) {
                rank[i] += x.level[i].span;
                x = x.level[i].forward;
            }
            update[i] = x;
        }
        /* we assume the element is not already inside, since we allow duplicated
         * scores, reinserting the same element should never happen since the
         * caller of zslInsert() should test in the hash table if the element is
         * already inside or not. */
        level = zslRandomLevel();
        if (level > this.level) {
            for (i = this.level; i < level; i++) {
                rank[i] = 0;
                update[i] = header;
                update[i].level[i].span = this.size;
            }
            this.level = level;
        }
        x = createNode(score, ele);
        for (i = 0; i < level; i++) {
            x.level[i].forward = update[i].level[i].forward;
            update[i].level[i].forward = x;

            /* update span covered by update[i] as x is inserted here */
            x.level[i].span = update[i].level[i].span - (rank[0] - rank[i]);
            update[i].level[i].span = (rank[0] - rank[i]) + 1;
        }

        /* increment span for untouched levels */
        for (i = level; i < this.level; i++) {
            update[i].level[i].span++;
        }

        x.backward = (update[0] == header) ? null : update[0];
        if (x.level[0].forward != null) {

            x.level[0].forward.backward = x;
        } else {
            tail = x;
            size++;
        }

        return x;
    }


    int zslRandomLevel() {
        int level = 1;
        while ((ThreadLocalRandom.current().nextLong() & 0xFFFF) < (ZSKIPLIST_P * 0xFFFF)) {
            level += 1;
        }
        return (level < ZSKIPLIST_MAXLEVEL) ? level : ZSKIPLIST_MAXLEVEL;
    }


    public E get(int index) {
        return null;
    }


    public Object[] toArray() {

        ZSkipListNode<E> node = header;

        if (node == null || size <= 0) {
            throw new UnsupportedOperationException("list is empty");
        }

        Object[] result = new Object[size];
        int i = 0;
        while (node != null) {
            result[i++] = node.ele;
        }
        return result;
    }


    public int size() {
        return size;
    }


    @Override
    public int compare(E o1, E o2) {
        return 0;
    }


    /**
     * 内部数据结构
     *
     * @param <E>
     */
    private class DataNode<E> {
        private int level;
        private E data;
        private DataNode prev;
        private DataNode next;

        public DataNode() {

        }

        public DataNode(int level, E data, DataNode prev, DataNode next) {
            this.level = level;
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }


    class ZSkipListNode<E> {

        private E ele;
        private double score;
        private ZSkipListNode<E> backward;
        private ZSkipListLevel<E>[] level;
    }

    class ZSkipListLevel<E> {
        private ZSkipListNode<E> forward;
        private long span;
    }


    //最底层一个有序链表
    // 上层是一个

    public static void main(String[] args) {

        CustomerSkipList<Integer> skipList = new CustomerSkipList<>();

    }


}
