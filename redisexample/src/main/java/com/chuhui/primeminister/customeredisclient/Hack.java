package com.chuhui.primeminister.customeredisclient;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Hack
 *
 * @author: cyzi
 * @Date: 2019/9/11 0011
 * @Description:TODO
 */
public class Hack {


    public static void main(String[] args) throws IOException {


        ServerSocket server=new ServerSocket(7002);

        while (true){

            Socket accept = server.accept();


            InputStream inputStream = accept.getInputStream();

            byte[] readBytes=new byte[1024];

            inputStream.read(readBytes);

            String s = new String(readBytes, "utf-8");

            System.err.println(s);



        }





    }





}
