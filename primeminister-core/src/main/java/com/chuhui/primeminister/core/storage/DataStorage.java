package com.chuhui.primeminister.core.storage;

/**
 * DataStorage
 *
 * @author: cyzi
 * @Date: 6/6/22
 * @Description:
 */
public interface DataStorage{



    // 整个调用链,是由什么组成的?
    // 节点
    // 一定要有一个唯一的traceId
    // traceId存在了,一起传送到第三方,我该怎么找起始点呢?

    // traceId的生成,就比较简单了,,,大不了,先搞一个UUID,然后根据
    // 针对的,一个是日志系统,一个是远程调用
    //
    // 起始点,是第一层链路-->
    // 在另外一个系统里面,我能知道是第几个链路进来的
    // 然后执行的时候,根据前一个链路,在当前的基础上加1
    //
    // 在第一个链路上,一次性发起多个远程调用,
    // 链路的层级+该层级下第几个请求
    // 日志系统,是属于第几个层级
    //
    // 如果日志和

    //

}
