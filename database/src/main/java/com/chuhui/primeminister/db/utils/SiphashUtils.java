package com.chuhui.primeminister.db.utils;

/**
 * SiphashUtils
 *
 * @author: 纯阳子
 * @Date: 2019/7/3 0003
 * @Description: SipHash参考 java语言实现
 */
public class SiphashUtils {

   public static  int siptlw(int c) {
        if (c >= 'A' && c <= 'Z') {
            return c+('a'-'A');
        } else {
            return c;
        }
    }

   //int ROTL(x, b) (uint64_t)(((x) << (b)) | ((x) >> (64 - (b))))

//如何改编呢?


}
