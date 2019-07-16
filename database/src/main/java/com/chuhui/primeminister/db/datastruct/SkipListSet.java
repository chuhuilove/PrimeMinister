package com.chuhui.primeminister.db.datastruct;

import java.util.*;

/**
 * SkipList
 * <p>
 * 吾辈既务斯业,便当专心用功;
 * 以后名扬四海,根据即在年轻.
 * 一个有序的,不重复的,内部使用skip list实现的set
 * <p>
 * 如果将其改造为一个list,会有很大的局限性
 *
 * @author: 纯阳子
 * @Date: 2019/7/16
 * @Description:TODO
 */
public class SkipListSet<E extends Comparable<? super E>> implements Set<E> {

    // https://blog.csdn.net/moakun/article/details/79997037

    /**
     * 该跳跃表实现,来自于:https://blog.csdn.net/moakun/article/details/79997037
     * 没测试,感觉好像缺点什么.
     * 2019年7月16日07:06:39
     * 完成java版redis的任务.任重道远啊
     * 2019年7月16日15:01:16
     * head和tail,用什么来区分呢?
     */


    private SkipListNode<E> head, tail;

    private int size;
    private int listLevel;
    private Random random;
    static final float PROBABILITY = 0.5f;


    public SkipListSet() {

        random = new Random();
        clear();

    }


    private SkipListNode<E> findeNode(E value) {


        SkipListNode<E> p = head;

        boolean flag = true;
        while (flag) {

            while (p.right != tail && p.right.value.compareTo(value) < 0) {
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





    public void put( E value) {

        SkipListNode<E> searchNode = findeNode(value);




//        if (searchNode.key == key) {
//            //执行更新操作
//            searchNode.value = value;
//            return;
//        }


        SkipListNode<E> inserNode = new SkipListNode<>(value);
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
                SkipListNode<E> p1 = new SkipListNode<>(null);

                SkipListNode<E> p2 = new SkipListNode<>(null);
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

            SkipListNode<E> eSkipListNode = new SkipListNode<>(null);
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

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return (head.right == tail || size <= 0);
    }

    @Override
    public boolean contains(Object o) {


        // set背后都有一个map
        //

        // 但是

//        findeNode(o);
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public boolean add(E e) {


        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }


    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

        size = 0;
        head.up = null;
        head.down = null;
        head.left = null;
        head.right = tail;

        tail.up = null;
        tail.down = null;
        tail.right = null;
        tail.left = head;


    }


    class SkipListNode<E> {

        E value;

        SkipListNode<E> up, down, left, right;

        SkipListNode(E value) {
            this.value = value;
        }


    }

    public static void main(String[] args) {


        // 如同HashSet和HashMap,
        // ConcurrentSkipListSet中使用的是ConcurrentSkipListMap

        SkipListSet<Integer> skiplist = new SkipListSet<>();

        for (int i = 0; i < 100; i++) {
            skiplist.put( i);
        }


    }


}
