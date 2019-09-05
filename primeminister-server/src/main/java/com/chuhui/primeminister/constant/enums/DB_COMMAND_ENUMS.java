package com.chuhui.primeminister.constant.enums;

/**
 * DB_COMMAND_ENUMS
 * <p>
 *
 * @author: 纯阳子
 * @Date: 2019/9/5
 * @Description:TODO
 */
public enum DB_COMMAND_ENUMS {


    /**
     *
     */
    SET("set"),
    GET("get"),
    SETNX("setnx"),
    SETEX("setex"),
    PSETEX("psetex"),
    APPEND("append");

//    {"get",getCommand,2,"rF",0,NULL,1,1,1,0,0},
//    {"set",setCommand,-3,"wm",0,NULL,1,1,1,0,0},
//    {"",setnxCommand,3,"wmF",0,NULL,1,1,1,0,0},
//    {"",setexCommand,4,"wm",0,NULL,1,1,1,0,0},
//    {"",psetexCommand,4,"wm",0,NULL,1,1,1,0,0},
//    {"",appendCommand,3,"wm",0,NULL,1,1,1,0,0}, 向key的value追加字符串 {"strlen",strlenCommand,2,"rF",0,NULL,1,1,1,0,0}, 返回指定key的value长度




    String commannd;

    DB_COMMAND_ENUMS(String commannd){
        this.commannd=commannd;
    }








}
