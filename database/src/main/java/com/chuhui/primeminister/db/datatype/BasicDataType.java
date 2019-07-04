package com.chuhui.primeminister.db.datatype;


import java.io.DataInputStream;
import java.nio.ByteBuffer;

/**
 * Unit64_t
 *
 * @author: 纯阳子
 * @Date: 2019/7/3 0003
 * @Description: 模拟c语言基本类型
 */
public abstract class BasicDataType {

    abstract public Object getData();

    /**
     * uint64_t
     */
    public static class Uint64_t extends BasicDataType {
        private long uint64_t;

        public Uint64_t() {
            this(0);
        }

        public Uint64_t(int data) {
            uint64_t = data & 0x0FFFFFFFFL;
        }

        @Override
        public Object getData() {

            ByteBuffer buffer=ByteBuffer.allocateDirect(10);


            return uint64_t;
        }



    }

    /**
     * 8 typedef unsigned char           uint8_t;
     */
    public static class Uint8_t extends BasicDataType {
        private int uint8_t;

        public Uint8_t() {
            this((byte)0);
        }

        public Uint8_t(byte data) {
            uint8_t = data & 0x0FF;

        }

        @Override
        public Object getData() {
            return uint8_t;
        }
    }




//    public int getUnsignedByte(byte data) {      //将data字节型数据转换为0~255 (0xFF 即BYTE)。
//        return data & 0x0FF;
//    }
//
//    public int getUnsignedByte(short data) {      //将data字节型数据转换为0~65535 (0xFFFF 即 WORD)。
//        return data & 0x0FFFF;
//    }
//
//    public long getUnsignedIntt(int data) {     //将int数据转换为0~4294967295 (0xFFFFFFFF即DWORD)。
//
//        return data & 0x0FFFFFFFFL;
//    }

}
