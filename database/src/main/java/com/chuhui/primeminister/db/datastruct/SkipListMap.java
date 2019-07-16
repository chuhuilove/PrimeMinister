package com.chuhui.primeminister.db.datastruct;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * SkipList
 * <p>
 * 吾辈既务斯业,便当专心用功;
 * 以后名扬四海,根据即在年轻.
 * 一个有序的,不重复的,内部使用skip list实现的map
 * <p>
 * 如果将其改造为一个list,会有很大的局限性
 *
 * @author: 纯阳子
 * @Date: 2019/7/16
 * @Description:TODO
 */
public class SkipListMap<K, V> extends AbstractMap<K, V> implements Map<K, V> {


    static final byte HEAD_KEY = Byte.MIN_VALUE;

    static final byte TAIL_KEY = Byte.MAX_VALUE;


    private Node<K, V> head, tail;

    final Comparator<? super K> comparator;

    private int size;
    private int listLevel;

    static final float PROBABILITY = 0.5f;

    public SkipListMap(Comparator<? super K> comparator) {
        this.comparator = comparator;
        init();
    }

    public SkipListMap() {

        this.comparator = null;
        init();
    }

    @Override
    public int size() {
        return size;
    }

    private void init() {

        head = new Node<>();
        tail = new Node<>();
        head.right = tail;
        tail.left = head;
        head.flag = HEAD_KEY;
        tail.flag = TAIL_KEY;


    }


    @Override
    public V put(K key, V value) {


        Node<K, V> searchNode = search(key);

        /**
         * 创建Node对象的时候,只有head和tail才会被赋值,其他情况下,默认为0
         */
        if (searchNode.flag != HEAD_KEY && searchNode.flag != TAIL_KEY && compare(searchNode.key, key) == 0) {
            // 只更新
            searchNode.value = value;
            return value;
        }


        Node<K, V> inserNode = new Node<>(key, value);
        backlist(searchNode, inserNode);


        // 当前所在层级是0
        int currentLevel = 0;

        // 抛硬币
        while (ThreadLocalRandom.current().nextFloat() < PROBABILITY) {


            /**
             * 如果超出了高度,则需要重新建一个顶层
             */
            if (currentLevel >= listLevel) {

                listLevel++;
                Node<K, V> p1 = new Node<>();
                p1.flag = HEAD_KEY;
                Node<K, V> p2 = new Node<>();
                p2.flag = TAIL_KEY;
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

            Node<K, V> eSkipListNode = new Node<>(key, value);
            backlist(searchNode, eSkipListNode);
            vertiacallLink(eSkipListNode, inserNode);
            inserNode = eSkipListNode;

            currentLevel++;

        }

        size++;

        return value;

    }


    private void backlist(Node<K, V> node1, Node<K, V> node2) {
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
    private void vertiacallLink(Node<K, V> node1, Node<K, V> node2) {
        node1.down = node2;
        node2.up = node1;
    }


    /**
     * 水平双向连接
     *
     * @param node1
     * @param node2
     */
    private void horizontalLink(Node<K, V> node1, Node<K, V> node2) {
        node1.right = node2;
        node2.left = node1;

    }


    @Override
    public boolean containsKey(Object key) {

        Node<K, V> searchRes = search(key);
        return searchRes.key == null;

    }


    private Node<K, V> search(Object key) {
        Node<K, V> p = head;

        boolean flag = true;
        while (flag) {

            while (p.right.flag != TAIL_KEY && compare(p.right.key, key) < 0) {
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


    final int compare(Object k1, Object k2) {

        return comparator == null ? ((Comparable<? super K>) k1).compareTo((K) k2)
                : comparator.compare((K) k1, (K) k2);
    }

    transient Set<Entry<K, V>> entrySet;


    @Override
    public Set<Entry<K, V>> entrySet() {



        Set<Map.Entry<K,V>> es;
        return (es = entrySet) == null ? (entrySet = new  EntrySet<>(this)) : es;

    }


    final class EntrySet<K1, V1> extends AbstractSet<Map.Entry<K, V>> {

        private SkipListMap map;

        public EntrySet(SkipListMap map) {
            this.map = map;
        }

        @Override
        public Iterator<Entry<K, V>> iterator() {
            Node p = map.head;

            while (p != null) {
                p = p.down;
            }

            SkipListIterator iterator = new SkipListIterator(p);

            return iterator;
        }

        @Override
        public int size() {
            return map.size();
        }
    }


    final class SkipListIterator implements Iterator<Entry<K, V>> {

        Node node;

        public SkipListIterator(Node node) {
            this.node = node;
        }

        @Override
        public boolean hasNext() {
            return node.right == null;
        }

        @Override
        public Entry<K, V> next() {
            return node.right;
        }
    }


    static class Node<K, V> implements Entry<K, V> {
        byte flag;

        K key;
        V value;

        Node<K, V> up, down, left, right;


        public Node() {
        }

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            this.value = value;
            return this.value;
        }
    }


    public static void main(String[] args) {


        SkipListMap<Integer, Integer> map = new SkipListMap<>();


        map.put(1, 1);
        map.put(2, 2);
        map.put(3, 3);
        map.put(5, 4);
        map.put(4, 5);
        map.put(6, 6);


        Set<Entry<Integer, Integer>> entries = map.entrySet();

        for (Entry<Integer, Integer> set : entries) {

            System.err.println(set.getKey() + "----->" + set.getValue());
        }


//        Map<Integer, Integer> hashMap = new HashMap<>();
//
//        for (int i = 0; i < 100; i++) {
//            hashMap.put(i, i);
//        }
//
//        Set<Entry<Integer, Integer>> entries = hashMap.entrySet();
//
//        for (Entry<Integer, Integer> set : entries) {
//
//            System.err.println(set.getKey() + "----->" + set.getValue());
//        }


//        Map<Integer,Integer> map=new TreeMap<>();


//        Scanner sc=new Scanner(System.in);
//
//        String str;
//        while (true){
//
//            str=sc.next();
//
//
//            str=str.replace("吗","");
//            str=str.replace("?","!");
//            str=str.replace("?","!");
//            System.err.println(str);
//
//        }


    }


}
