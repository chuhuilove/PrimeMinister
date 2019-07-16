package com.chuhui.primeminister;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * RedisClient
 *
 * @author: 纯阳子
 * @Date: 2019/7/4 0004
 * @Description:TODO
 */
public class RedisClient {


    public static void main(String[] args) {

        try( Socket socket=new Socket("118.24.141.172",8000)) {

            for(;;) {
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write("woaini".getBytes());

                InputStream inputStream = socket.getInputStream();

                byte[] readBytes = new byte[10];

                inputStream.read(readBytes);

                System.err.println("data:" + new String(readBytes, "UTF-8"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }



}
