package com.chuhui.primeminister.constant;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Constants
 *
 * @author: 纯阳子
 * @Date: 2019/7/3 0003
 * @Description:TODO
 */
public class Constants {

    final public static int PORT = 13225;
    final public static String HOST = "127.0.0.1";

    final public static String DEFAULT_CONFIG_FILENAME = "PrimeMinisterConf.yml";


    private static final String DELIMITER = "<PMDB>";
    private static final String RESERVE_DELIMITER = "</PMDB>";

    public final static ByteBuf PMDBBUF = Unpooled.copiedBuffer(DELIMITER.getBytes());
    public final static ByteBuf RESERVEPMDBBUF = Unpooled.copiedBuffer(RESERVE_DELIMITER.getBytes());


    public enum ACTUAL_REDIS_OBJECT_ENUMS {

    }


    public static void main(String[] args) {
//
//        ACTUAL_REDIS_OBJECT_ENUMS objHash = ACTUAL_REDIS_OBJECT_ENUMS.OBJ_HASH;
//
//        System.err.println(objHash.typeName);


    }


}
