package com.chuhui.primeminister.db.datastruct;

import java.util.Set;
import java.util.TreeSet;

/**
 * TreeSetTest
 * <p>
 * 吾辈既务斯业,便当专心用功;
 * 以后名扬四海,根据即在年轻.
 *
 * @author: 纯阳子
 * @Date: 2019/7/18
 * @Description:TODO
 */
public class TreeSetTest {


    public static void main(String[] args) {


        /**
         * 默认情况下,字符串的排序是根据其字典顺序来做的
         */

        Set<String> set = new TreeSet<>();


        for (int i = 1; i <= 1000; i++) {

            set.add("cyzi" + i);
        }


        for (String str : set) {

            System.err.println(str);

        }


    }


}
