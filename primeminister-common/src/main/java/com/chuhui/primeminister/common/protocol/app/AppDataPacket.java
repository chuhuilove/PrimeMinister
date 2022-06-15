package com.chuhui.primeminister.common.protocol.app;

import lombok.Data;

/**
 * AppDataPacket
 * 应用数据包
 *
 * @author: cyzi
 * @Date: 6/14/22
 * @Description:
 */
@Data
public class AppDataPacket {


    /**
     * 0 日志 1 方法调用
     */
   private byte dataType;
   private long timestamp;

   private String traceId;




}
