package com.chuhui.primeminister.common.protocol.inner;

import lombok.Data;

/**
 * HeartDataDefinition
 * 客户端发向服务端的心跳包
 * @author: cyzi
 * @Date: 6/14/22
 * @Description:
 */
@Data
public class HeartDataDefinition implements InnerDataDefinition {
   private byte heart=-1;
}
