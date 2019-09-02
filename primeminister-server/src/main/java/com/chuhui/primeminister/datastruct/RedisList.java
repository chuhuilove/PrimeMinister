package com.chuhui.primeminister.datastruct;


/**
 * RedisADList
 * redis中通用双向链表的实现 adlist.h he adlist.c
 * 该链表不是线程安全的
 *
 * @author: 纯阳子
 * @Date: 2019/6/28 0028
 */
public class RedisList<E> implements Cloneable {


    private static final int AL_START_HEAD = 0;
    private static final int AL_START_TAIL = 1;


    private ListNode<E> head;
    private ListNode<E> tail;

    private long len;


    public RedisList() {
        head = null;
        tail = null;
        len = 0;
    }

    public void listEmpty() {
        if (len != 0) {
            ListNode prevNode = head;

            while (prevNode != null) {
                prevNode = prevNode.next;
                len--;
            }
            head = null;
            tail = null;
        }
    }

    /**
     * 将元素添加到{@link RedisList#head}的前面,作为新的{@link RedisList#head}
     *
     * @param value
     */
    public void listAddNodeHead(E value) {
        listAddNodeHead(this, value);
    }


    /**
     * 添加节点到链表尾,取代现有的{@link RedisList#tail},添加的节点会成为新的{@link RedisList#tail}
     *
     * @param value
     */
    public void listAddNodeTail(E value) {

        listAddNodeTail(this, value);
    }


    private ListIter<E> listGetIterator(int direction) {

        return listGetIterator(this, direction);
    }


    public ListNode<E> listNext(ListIter<E> iter) {
        ListNode<E> current = iter.next;

        if (current != null) {
            if (iter.direction == AL_START_HEAD) {

                iter.next = current.next;
            } else {
                iter.next = current.prev;
            }
        }
        return current;
    }

    public void listRewind(RedisList<E> list, ListIter<E> li) {
        li.next = list.head;
        li.direction = AL_START_HEAD;
    }

    public void listRewindTail(RedisList<E> list, ListIter<E> li) {
        li.next = list.tail;
        li.direction = AL_START_TAIL;
    }

    /**
     * 默认实现了{@link Cloneable} 接口,在不重写clone的情况下,调用clone函数,返回的实例是浅拷贝.
     * 而不实现{@link Cloneable} 接口,调用clone方法,会返回一个{@link CloneNotSupportedException}异常
     * <p>
     * 这里重写了clone方法,实现了深拷贝
     *
     * @return
     */
    @Override
    public RedisList<E> clone() {

        return listDup(this);
    }

    public void listDelNode(ListNode<E> node) {

        listDelNode(this, node);
    }


    /**
     * 搜索key所在的节点
     * ps: 若要使用此方法,泛型E必须重写了equals方法
     *
     * @param key
     * @return
     */
    public ListNode<E> listSearchKey(E key) {

        ListIter<E> iter = new ListIter<>();
        ListNode<E> node;

        listRewind(this, iter);
        while ((node = listNext(iter)) != null) {
            if (node.equals(key)) {
                return node;
            }
        }
        return null;
    }

    public ListNode<E> listIndex(long index) {
        return listIndex(this, index);
    }


    public void listRotate() {

        listRotate(this);
    }

    /**
     * 在指定位置插入一个数据节点
     *
     * @param value
     * @param after
     */
    public void listInsertNode(E value, ListNode<E> oldNode, int after) {

        listInsertNode(this, oldNode, value, after);
    }

    static <E> void listInsertNode(RedisList<E> list, ListNode<E> oldNode, E value, int after) {

        ListNode<E> node = new ListNode<>(value);


        if (after != 0) {
            node.prev = oldNode;
            node.next = oldNode.next;
            if (list.tail == oldNode) {

                list.tail = node;
            }
        } else {
            node.next = oldNode;
            node.prev = oldNode.prev;
            if (list.head == oldNode) {
                list.head = node;
            }
        }
        if (node.prev != null) {
            node.prev.next = node;
        }
        if (node.next != null) {
            node.next.prev = node;
        }
        list.len++;
    }


    private RedisList<E> listDup(RedisList<E> orig) {


        RedisList<E> copy = new RedisList<>();
        ListIter<E> iter = listGetIterator(AL_START_HEAD);
        ListNode<E> node;


        listRewind(orig, iter);
        while ((node = listNext(iter)) != null) {
            listAddNodeTail(copy, node.value);
        }
        return copy;
    }


    /**
     * 连接两个链表
     *
     * @param l
     * @param o
     * @return
     */
    public static RedisList listJoin(RedisList l, RedisList o) {

        o.head.prev = l.tail;
        l.tail.next = o.head;
        l.len = l.len + o.len;
        l.tail = o.tail;

        o.head = o.tail = null;
        o.len = 0;
        return l;
    }

    public long size() {
        return len;
    }

    /*******************static method and static Internal class******************************/

    static <E> ListNode<E> listIndex(RedisList<E> list, long index) {
        ListNode<E> n;

        if (index < 0) {
            index = (-index) - 1;
            n = list.tail;
            while (index-- > 0 && n != null) {
                n = n.prev;
            }

        } else {
            n = list.head;
            while (index-- > 0 && n != null) {
                n = n.next;
            }
        }
        return n;
    }

    static <E> void listDelNode(RedisList<E> list, ListNode<E> node) {
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            list.head = node.next;
        }
        if (node.next != null) {
            node.next.prev = node.prev;
        } else {

            list.tail = node.prev;
        }

        list.len--;
    }

    static <E> ListIter<E> listGetIterator(RedisList<E> list, int direction) {
        ListIter<E> iter = new ListIter<>();

        if (direction == AL_START_HEAD) {
            iter.next = list.head;
        } else {

            iter.next = list.tail;
        }
        iter.direction = direction;
        return iter;
    }

    static <E> void listAddNodeHead(RedisList<E> list, E value) {

        ListNode<E> node = new ListNode<>();
        node.value = value;

        // 如果当前没有元素
        if (list.len == 0) {
            list.head = list.tail = node;
            node.prev = node.next = null;
        } else {
            list.head.prev = node;
            node.prev = null;
            node.next = list.head;
            list.head = node;
        }
        list.len++;
    }

    static <E> void listAddNodeTail(RedisList<E> list, E value) {

        ListNode<E> node = new ListNode<>(value);
        if (list.len == 0) {
            list.head = list.tail = node;
            node.prev = node.next = null;
        } else {
            node.next = null;
            list.tail.next = node;
            node.prev = list.tail;
            list.tail = node;
        }
        list.len++;
    }

    static <E> void listRotate(RedisList<E> list) {

        ListNode<E> tail = list.tail;

        if (list.len <= 1) {
            return;
        }


        list.tail = tail.prev;
        list.tail.next = null;

        list.head.prev = tail;
        tail.prev = null;
        tail.next = list.head;
        list.head = tail;
    }


    public static class ListNode<E> {
        private ListNode<E> prev;
        private ListNode<E> next;
        private E value;

        ListNode() {
        }

        ListNode(E value) {
            this.value = value;
        }
    }


    static class ListIter<E> {
        ListNode<E> next;
        int direction;
    }


}
