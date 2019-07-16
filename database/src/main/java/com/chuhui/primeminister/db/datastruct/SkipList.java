package com.chuhui.primeminister.db.datastruct;

import java.util.Random;

/**
 * SkipList
 * <p>
 * 吾辈既务斯业,便当专心用功;
 * 以后名扬四海,根据即在年轻.
 *
 * @author: 纯阳子
 * @Date: 2019/7/16
 * @Description:TODO
 */
public class SkipList<E extends Comparable<? super E>> {


    /**
     * 该跳跃表实现,来自于:https://blog.csdn.net/moakun/article/details/79997037
     * 没测试,感觉好像缺点什么.
     * 2019年7月16日07:06:39
     * 完成java版redis的任务.任重道远啊
     * 2019年7月16日19:24:01
     * 测试,可以..不能用跳跃表实现lits接口
     * list有一个set(int index,E element) 接口,使用跳跃表无法实现
     */

    private SkipListNode<E> head, tail;

    private int size;
    private int listLevel;
    private Random random;
    static final float PROBABILITY = 0.5f;


    public SkipList() {

        random = new Random();
        clear();

    }

    public void clear() {

        head = new SkipListNode<>(SkipListNode.HEAD_KEY, null);
        tail = new SkipListNode<>(SkipListNode.TAIL_KEY, null);
        horizontalLink(head, tail);
        listLevel = 0;
        size = 0;
    }


    private SkipListNode<E> findeNode(int key) {

        SkipListNode<E> p = head;

        boolean flag = true;
        while (flag) {

            while (p.right.key != SkipListNode.TAIL_KEY && p.right.key <= key) {
                p = p.right;
            }

            if (p.down != null) {
                p = p.down;
            } else {
                flag = false;
            }

        }
        return p;
    }


    public SkipListNode<E> search(int key) {

        SkipListNode<E> findeNode = findeNode(key);


        if (findeNode.key == key) {
            return findeNode;
        }

        return null;

    }


    public void put(int key, E value) {

        SkipListNode<E> searchNode = findeNode(key);

        if (searchNode.key == key) {
            //执行更新操作
            searchNode.value = value;
            return;
        }


        SkipListNode<E> inserNode = new SkipListNode<>(key, value);
        backlist(searchNode, inserNode);


        // 当前所在层级是0
        int currentLevel = 0;

        // 抛硬币
        while (random.nextFloat() < PROBABILITY) {


            /**
             * 如果超出了高度,则需要重新建一个顶层
             */
            if (currentLevel >= listLevel) {

                listLevel++;
                SkipListNode<E> p1 = new SkipListNode<>(SkipListNode.HEAD_KEY, null);

                SkipListNode<E> p2 = new SkipListNode<>(SkipListNode.TAIL_KEY, null);
                horizontalLink(p1, p2);
                vertiacallLink(p1, head);
                vertiacallLink(p2, tail);
                head = p1;
                tail = p2;
            }

            while (searchNode.up == null) {
                searchNode = searchNode.left;
            }

            searchNode = searchNode.up;

            SkipListNode<E> eSkipListNode = new SkipListNode<>(key, null);
            backlist(searchNode, eSkipListNode);
            vertiacallLink(eSkipListNode, inserNode);
            inserNode = eSkipListNode;

            currentLevel++;

        }

        size++;

    }


    /**
     * 在node1后面插入node2
     *
     * @param node1
     * @param node2
     */
    private void backlist(SkipListNode<E> node1, SkipListNode<E> node2) {
        node2.left = node1;
        node2.right = node1.right;
        node1.right.left = node2;
        node1.right = node2;

    }

    /**
     * 垂直双向连接
     *
     * @param node1
     * @param node2
     */
    private void vertiacallLink(SkipListNode<E> node1, SkipListNode<E> node2) {
        node1.down = node2;
        node2.up = node1;
    }


    /**
     * 水平双向连接
     *
     * @param node1
     * @param node2
     */
    private void horizontalLink(SkipListNode<E> node1, SkipListNode<E> node2) {

        node1.right = node2;
        node2.left = node1;

    }


    class SkipListNode<E extends Comparable<? super E>> {
        int key;
        E value;

        SkipListNode<E> up, down, left, right;
        static final int HEAD_KEY = Integer.MIN_VALUE;
        static final int TAIL_KEY = Integer.MAX_VALUE;

        public SkipListNode(int key, E value) {
            this.key = key;
            this.value = value;
        }
    }

}
