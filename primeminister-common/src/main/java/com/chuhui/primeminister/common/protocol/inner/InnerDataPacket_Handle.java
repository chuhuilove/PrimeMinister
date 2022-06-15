package com.chuhui.primeminister.common.protocol.inner;

import lombok.Data;

/**
 * InnerDataPacket
 * 客户端和服务端之间的数据包
 *
 * @author: cyzi
 * @Date: 6/14/22
 * @Description:
 */
@Data
public class InnerDataPacket_Handle {

    /**
     * 1 注册 2 心跳
     */
    private byte dataType;

    private InnerDataDefinition innerDataDefinition;

}
