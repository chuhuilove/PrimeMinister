package com.chuhui.primeminister.db.datastruct;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.IntStream;

/**
 * RedisDict
 * redis中的hash表实现.
 * redis中的hash表本质上采用链表进行存储
 * <p>
 * 2019年7月15日10:44:39
 * 使用跳跃表实现一个hashMap
 * 这个hash表,内部由跳跃表实现,不采用红黑树了
 *
 * @author: 纯阳子
 * @Date: 2019/6/28 0028
 * @Description:
 */
public class RedisDict<K, V> extends AbstractMap<K, V> implements Map<K, V> {
    @Override
    public Set<Entry<K, V>> entrySet() {


        return null;
    }


    public static void main(String[] args) {


        Map<String, Integer> skiplistMap = new ConcurrentSkipListMap<>();


        for (int i = 1; i <= 20; i++) {

            skiplistMap.put("xcc" + i, i);

        }


    }



















/*
    private static final int DICT_OK = 0;
    private static final int DICT_ERR = 1;
    private static final int DICT_HT_INITIAL_SIZE = 4;

    private static int DICT_CAN_RESIZE = 1;

    private int rehashidx = -1;
    private long iterators;

    private long size;
    private long used;


    public RedisDict() {

    }


    public boolean dictAdd(E key, V val) {
        DictEntry entry = dictAddRaw(key, null);

        if (entry != null) {
            return false;
        }

//        dictSetVal(d, entry, val);
        return true;
    }


    public DictEntry dictAddRaw(E key, DictEntry existing) {

        if (dictIsRehashing()) {
            dictRehashStep();
        }


        long index;

        if ((index = dictKeyIndex(key, dictHashKey(key), existing)) == -1) {

            return null;
        }


        return null;
    }

    class DictEntry<E, V> {

        private E key;
        private V value;
        private DictEntry next;

    }

//    typedef struct dictht {
//        dictEntry **table;
//        unsigned long size;
//        unsigned long sizemask;
//        unsigned long used;
//    } dictht;


    final int dictKeyIndex(E key, int hash, DictEntry existing) {
        long idx, table;
        DictEntry he;


        if (dictExpandIfNeeded() == 1) {

            return -1;
        }
        return 0;
    }

    final int dictExpandIfNeeded() {

        if (dictIsRehashing()) {
            return DICT_OK;
        }
        if (size == 0) {
            return dictExpand(DICT_HT_INITIAL_SIZE);
        }

        return 0;
    }

    private int dictExpand(int size) {
        if (dictIsRehashing() || used > size) {
            return DICT_ERR;
        }
        //TODO 扩展hash表
        return 0;
    }


    final int dictHashKey(E key) {

        return System.identityHashCode(key);

    }

    final boolean dictRehash(int n) {
        //TODO 重新hash
        return false;
    }

    final void dictRehashStep() {
        if (iterators == 0) {
            dictRehash(1);
        }
    }


    final boolean dictIsRehashing() {
        return rehashidx != -1;
    }


    void dictEnableResize() {
        DICT_CAN_RESIZE = 1;
    }

    void dictDisableResize() {
        DICT_CAN_RESIZE = 0;
    }
*/
    /**
     * 万事开头难,只要挺过这一阵,就好了....
     * //FIXME 2019年7月1日12:01:34 by 纯阳子
     */

}
