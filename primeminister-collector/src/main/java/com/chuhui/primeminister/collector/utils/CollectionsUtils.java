package com.chuhui.primeminister.collector.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * CollectionsUtils
 *
 * @author: cyzi
 * @Date: 6/9/22
 * @Description:
 */
final public class CollectionsUtils {

    private CollectionsUtils() {
    }


    public static boolean isEmpty(Collection<?> collection) {
        return Objects.isNull(collection) || collection.size() <= 0;
    }


    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }


    public static <E> List<E> newArrayList(E e) {
        List<E> ret = new ArrayList<>();
        ret.add(e);
        return ret;
    }


}
