package com.chuhui.primeminister.db.datastruct;

import sun.reflect.generics.tree.Tree;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

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

            while (p.right.flag != TAIL_KEY && compare(p.right.key, key) <=0) {
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


    /**
     * 还有很多问题出现.暂时不能急着迭代
     *
     * @return
     */
    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
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



    static class KeySet<E> extends AbstractSet<E>{


        private SkipListMap map;


        @Override
        public Iterator<E> iterator() {






            return null;
        }

        @Override
        public int size() {
            return 0;
        }
    }

    static class KetSetIterator<E> implements  Iterator<E>{

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public E next() {
            return null;
        }
    }




    /*
        static class EntrySet<K1, V1> extends AbstractSet<Map.Entry<K1, V1>> {

            private SkipListMap map;

            public EntrySet(SkipListMap  map) {
                this.map = map;
            }

            @Override
            public Iterator<Entry<K1, V1>> iterator() {

                // 做迭代器
                // 真的能获得不少东西啊
                // 2019年7月16日20:45:05
                // 好好捣鼓一下迭代器
                //
                Node  p =  map.head;

                while (p != null) {
                    p = p.down;
                }
                return new SkiplistIterator(p);

            }

            @Override
            public int size() {
                return map.size();
            }
        }
    */
    Node<K, V> getHead() {
        return head;
    }

    static class SkiplistIterator implements Iterator<Node> {

        Node node;

        public SkiplistIterator(Node node) {
            this.node = node;
        }

        @Override
        public boolean hasNext() {
            return node.right == null;
        }

        @Override
        public Node next() {
            return null;
        }

//        public Node<K, V> next() {
//            return node.right;
//        }
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

    public Node<K, V> getHeade() {
        return head;
    }


    public static void main(String[] args) {

        SkipListMap<String, Integer> map = new SkipListMap<>();


        int count = 1000;

        Random random = new Random();

        int nextInt;
        while (count >= 0) {
            nextInt = random.nextInt();

            map.put("xcc"+nextInt, count--);
            map.put("xcc"+nextInt, count*100);
        }

        Node<String, Integer> head = map.getHead();


        while (true) {
            if (head.down == null) {
                break;
            }
            head = head.down;

        }

        // 证明一个数组是有序的


        byte tail=Byte.MAX_VALUE;
        while (head.right.flag != tail) {

            Node<String, Integer> dataNode = head.right;

            if(dataNode.key==null && dataNode.value==null){
                System.err.println("全是null");
                System.err.println("全是null");
                System.err.println("全是null");
            }
                System.err.println(dataNode.key + "--->" + dataNode.value);


            head = dataNode;



        }


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
