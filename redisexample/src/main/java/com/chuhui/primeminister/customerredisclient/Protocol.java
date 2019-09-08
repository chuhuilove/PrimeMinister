package com.chuhui.primeminister.customerredisclient;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Protocol
 * <p>
 * //TODO description
 *
 * @author: 纯阳子
 * @date: 2019/9/8
 */
public class Protocol {
    private static final String DOLLAR_BYTE = "$";
    private static final String ASTERISK_BYTE = "*";
    private static final String BLANK_STRING = "\r\n";


    public static void sendCommand(OutputStream os, Command command, byte[]... args) {

        StringBuilder sb = new StringBuilder();

        sb.append(ASTERISK_BYTE).append(args.length + 1).append(BLANK_STRING);
        sb.append(DOLLAR_BYTE).append(command.name().length() + 1).append(BLANK_STRING);
        sb.append(command.name()).append(BLANK_STRING);
        sb.append(command.name()).append(BLANK_STRING);

        for (final byte[] arg : args) {
            sb.append(DOLLAR_BYTE).append(arg.length).append(BLANK_STRING);
            sb.append(new String(arg)).append(BLANK_STRING);
        }

        try {
            os.write(sb.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public   enum Command {
        GET, SET, KEYS
    }


}
