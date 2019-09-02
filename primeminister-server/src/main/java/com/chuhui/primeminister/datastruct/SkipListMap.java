package com.chuhui.primeminister.datastruct;


import java.util.*;


/**
 * SkipList
 * <p>
 * 吾辈既务斯业,便当专心用功;
 * 以后名扬四海,根据即在年轻.
 * 一个有序的,不重复的,内部使用skip list实现的map
 * 此map不是线程安全的
 *
 * @author: 纯阳子
 * @Date: 2019/7/16
 * @Description:TODO
 */
public class SkipListMap<K, V> extends AbstractMap<K, V> implements Map<K, V> {

    private static final byte HEAD_KEY = Byte.MIN_VALUE;
    private static final byte TAIL_KEY = Byte.MAX_VALUE;


    private Node<K, V> head, tail;

    private final Comparator<? super K> comparator;

    /**
     * map的大小
     */
    private int size;
    /**
     * skip list 层数
     */
    private int listLevel;

    /**
     * 抛硬币的比较因子
     */
    private static final float PROBABILITY = 0.5f;

    private final Random random = new Random();

    public SkipListMap() {

        this.comparator = null;
        init();
    }

    public SkipListMap(Comparator<? super K> comparator) {
        this.comparator = comparator;
        init();
    }


    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean containsKey(Object key) {
        Node<K, V> searchRes = findNode(key);
        return searchRes.key == null;
    }

    @Override
    public V get(Object key) {
        Node<K, V> search = findNode(key);
        return search.value;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {

        Set<? extends Entry<? extends K, ? extends V>> entries = m.entrySet();

        if (entries.size() > 0) {

            for (Entry<? extends K, ? extends V> entry : m.entrySet()) {
                put(entry.getKey(), entry.getValue());
            }
        }
    }


    @Override
    public V put(K key, V value) {

        Node<K, V> searchNode = findNode(key);

        /* 创建Node对象的时候,只有head和tail的flag才会被赋值,其他情况下,默认为0*/
        if (searchNode.flag != HEAD_KEY && searchNode.flag != TAIL_KEY && compare(searchNode.key, key) == 0) {
            // 更新
            searchNode.value = value;
            return value;
        }


        Node<K, V> inserNode = new Node<>(key, value);
        backlist(searchNode, inserNode);


        // 从最底层开始创建索引,即第0层
        int currentLevel = 0;

        // 抛硬币
        while (random.nextFloat() < PROBABILITY) {

            /**
             * 如果超出了高度,则需要重新建一个顶层
             */
            if (currentLevel >= listLevel) {

                /*
                 * 创建新的head和tail
                 *
                 */
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

    @Override
    public V remove(Object key) {
        // 获取到的findNode是第0层
        Node<K, V> findNode = findNode(key);

        if (checkIsFlageNode(findNode)) {
            /* key 不存在*/
            throw new NoSuchElementException("This key is not exist");
        }

        Node<K, V> topNode = findNode.up;

        while (topNode != null) {
            topNode.right.left = topNode.left;
            topNode.left.right = topNode.right;
            topNode = topNode.up;
        }

        findNode.right.left = findNode.left;
        findNode.left.right = findNode.right;


        if (findNode.up != null) {
            Node<K, V> headPoint = this.head;
            Node<K, V> tailPoint = this.tail;

            while (headPoint != null) {

                if (headPoint.right.flag == TAIL_KEY) {
                    // 这一层,不要了..
                    listLevel--;
                    head = headPoint.down;
                    tail = tailPoint.down;
                    head.up = null;
                    tail.up = null;
                    headPoint = head;
                    tailPoint = tail;
                    // 最后一层
                    if (headPoint.down == null) {
                        break;
                    }
                } else {
                    headPoint = headPoint.down;
                }
            }
        }

        V value = findNode.value;
        size--;
        return value;
    }


    /**
     * @return
     */
    @Override
    public Set<Entry<K, V>> entrySet() {

        // 返回的是一个迭代器


        return null;
    }

    /**
     * 还有很多问题出现.暂时不能急着迭代
     *
     * @return
     */
    @Override
    public Set<K> keySet() {
        return new SkipListInsideSet<>(this);
    }


    /**
     * 内部set集合
     * TODO 这个该如何实现
     * @param <K>
     */
    class SkipListInsideSet<K> extends AbstractSet<K> {

        private SkipListMap<K, V> map;


        SkipListInsideSet(SkipListMap map) {
            this.map = map;
        }

        @Override
        public Iterator<K> iterator() {


            return null;
        }

        @Override
        public int size() {
            return map.size;
        }
    }


    /**
     * 内部迭代器实现
     * TODO 这个该怎么实现
     * @param <K>
     */
    class SkipListInsideIterator<K> implements Iterator<K> {

        private SkipListMap<K, V> map;

        SkipListInsideIterator(SkipListMap<K, V> map) {
            this.map = map;
        }

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public K next() {
            return null;
        }
    }

    static class Node<K, V> implements Entry<K, V> {
        byte flag;

        K key;
        V value;

        Node<K, V> up, down, left, right;


        Node() {
        }

        Node(K key, V value) {
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


    private void init() {

        head = new Node<>();
        tail = new Node<>();
        head.right = tail;
        tail.left = head;
        head.flag = HEAD_KEY;
        tail.flag = TAIL_KEY;
    }


    /**
     * 将node2插入到node1的后面
     *
     * @param node1
     * @param node2
     */
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


    /**
     * 根据key从表中获取节点
     *
     * @param key
     * @return
     */
    private Node<K, V> findNode(Object key) {
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


    /**
     * 判断节点是否为flag节点
     *
     * @param node
     * @return
     */
    private boolean checkIsFlageNode(Node<K, V> node) {

        return (node.flag == HEAD_KEY || node.flag == TAIL_KEY);
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


    public static void main(String[] args) {


        SkipListMap<Integer, Integer> map = new SkipListMap<>();


        int count = 100;


        while (count >= 0) {
            map.put(count, (count-- * 100));
        }


        Set<Integer> integerSet = map.keySet();


        Node<Integer, Integer> head = map.head;


        while (true) {
            if (head.down == null) {
                break;
            }
            head = head.down;

        }


        // 获取到所有带有up指针的节点
        List<Node<Integer, Integer>> ups = new LinkedList<>();

        while (head.right.flag != SkipListMap.TAIL_KEY) {

            Node<Integer, Integer> dataNode = head.right;

            if (dataNode.up != null) {
                ups.add(dataNode);
            }
            head = dataNode;
        }


        // 开始删除

        int size = map.size;

        for (Node node : ups) {


            map.remove(node.key);
        }


        int i = size - ups.size();


        if (i == map.size && map.listLevel == 0) {

            System.err.println("全部删除成功,准备二次删除");

        }


        count = 10000;


        while (count >= 0) {
            map.put(count, (count-- * 100));
        }

        head = map.head;

        while (true) {
            if (head.down == null) {
                break;
            }
            head = head.down;

        }


        // 获取到所有带有up指针的节点
        ups = new LinkedList<>();

        while (head.right.flag != SkipListMap.TAIL_KEY) {

            Node<Integer, Integer> dataNode = head.right;

            if (dataNode.up != null) {
                ups.add(dataNode);
            }
            head = dataNode;
        }


        // 开始删除

        size = map.size;

        Collections.shuffle(ups);

        for (Node node : ups) {


            map.remove(node.key);
        }


        i = size - ups.size();


        if (i == map.size && map.listLevel == 0) {

            System.err.println("全部删除成功");

        }

    }

    /**
     * 迭代每一个
     */
    static void iterator() {
        byte tail = Byte.MAX_VALUE;

        Node<String, Integer> head = null;

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
