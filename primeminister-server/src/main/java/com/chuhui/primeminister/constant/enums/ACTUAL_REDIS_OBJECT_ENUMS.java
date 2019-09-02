package com.chuhui.primeminister.constant.enums;

/**
 * ACTUAL_REDIS_OBJECT_ENUMS
 *
 * @author: 纯阳子
 * @Date: 2019/7/3 0003
 * @Description:TODO
 */
public enum ACTUAL_REDIS_OBJECT_ENUMS {



    /**
     * String object
     */
    OBJ_STRING(0, "String object."),
    /**
     * List object
     */
    OBJ_LIST(1, "List object"),
    /**
     * Set object
     */
    OBJ_SET(2, "Set object"),
    /**
     * Sorted set object
     */
    OBJ_ZSET(3, "Sorted set object"),
    /**
     * Hash object
     */
    OBJ_HASH(4, "Hash object");



    private int val;
    private String typeName;

    ACTUAL_REDIS_OBJECT_ENUMS(int val, String typeName) {

        this.val = val;
        this.typeName = typeName;

    }


}
