package com.chuhui.primeminister.collector.utils;

import java.util.Objects;

/**
 * StringUtils
 *
 * @author: cyzi
 * @Date: 6/9/22
 * @Description:
 */
final public class StringUtils {

    private StringUtils() {
    }


    public static  boolean isEmpty(CharSequence sequence){
        return Objects.isNull(sequence) || sequence.length()<=0;
    }


    public static  boolean isNotEmpty(CharSequence sequence){
        return !isEmpty(sequence);
    }


}
