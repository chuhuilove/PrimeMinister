package com.chuhui.primeminister.db.datastruct;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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

    private final Random random = new Random();

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
    public V get(Object key) {
        Node<K, V> search = search(key);
        return search.value;
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
        while (random.nextFloat() < PROBABILITY) {


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

            while (p.right.flag != TAIL_KEY && compare(p.right.key, key) <= 0) {
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


    /**
     * 还有很多问题出现.暂时不能急着迭代
     *
     * @return
     */
    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }

    @Override
    public V remove(Object key) {
        Node<K, V> findNode = search(key);

        if (checkIsFlageNode(findNode)) {
            // key 不存在
            return null;
        }
        /**
         * 删除分为两种情况:
         * 1. 节点的up指针上没有任何东西,即一个普通的节点
         *
         *      获取到这个节点之后,调整左右指针即可
         *
         * 2. 节点的up指针上有节点,该节点是一个标志节点
         *
         *     除调整左右指针外,还需要考虑up指针上的东西
         *
         *
         */
        //
        // 如果这个节点上面有东西,则连带一块删除


        // 只是一个子节点而已,上面没有任何东西
        if (findNode.up == null) {
            /**
             * findNode右侧的节点的左指针,指向findNode左侧的节点
             * findNode左侧的节点的右指针,指向findNode右侧的节点
             */
            findNode.right.left = findNode.left;
            findNode.left.right = findNode.right;

            return findNode.value;
        }


        V value=findNode.value;

        // 删除,需要好好测试一下下
        do {

            findNode.right.left = findNode.left;
            findNode.left.right = findNode.right;

            findNode=findNode.up;

        }while (findNode.up!=null);



        return value;
    }


    /**
     * 判断节点是否为flag节点
     *
     * @param node
     * @return
     */
    boolean checkIsFlageNode(Node<K, V> node) {

        return (node.flag == HEAD_KEY || node.flag == TAIL_KEY);
    }


    /**
     * 还有很多问题出现.暂时不能急着迭代
     *
     * @return
     */
    @Override
    public Set<K> keySet() {


        HashMap hashMap = new HashMap();

        TreeMap treeMap = new TreeMap();

        ConcurrentHashMap conHashMap = new ConcurrentHashMap();

        LinkedHashMap linkHashMap = new LinkedHashMap();

        Node p = head;

        while (p != null) {
            p = p.down;
        }

        return super.keySet();
    }


    static class KetSetIterator<E> implements Iterator<E> {

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public E next() {
            return null;
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

    public Node<K, V> getHead() {
        return head;
    }


    public static void main(String[] args) {

        SkipListMap<String, Integer> map = new SkipListMap<>();


        int count = 1000;


        while (count >= 0) {

            map.put("cyzi" + count, count--);
            map.put("cyzi" + count, count * 100);
        }

        Node<String, Integer> head = map.getHead();


        while (true) {
            if (head.down == null) {
                break;
            }
            head = head.down;

        }


        //todo 需要测试一下删除




        // 证明一个数组是有序的


        byte tail = Byte.MAX_VALUE;
        while (head.right.flag != tail) {

            Node<String, Integer> dataNode = head.right;

            if (dataNode.key == null && dataNode.value == null) {
                System.err.println("全是null");
                System.err.println("全是null");
                System.err.println("全是null");
            }
            System.err.println(dataNode.key + "--->" + dataNode.value);


            head = dataNode;

        }

    }

}
