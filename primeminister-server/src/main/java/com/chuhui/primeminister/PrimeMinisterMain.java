package com.chuhui.primeminister;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * PrimeMinisterMain
 *
 * @author: cyzi
 * @Date: 2019/11/6 0006
 * @Description: 启动命令行终端
 */
public class PrimeMinisterMain {

    private long commandCount = 0;


    static final Map<String, String> commandMap = new HashMap<String, String>();

    static {
        commandMap.put("connect", "host:port");
        commandMap.put("history", "");
        commandMap.put("redo", "cmdno");
        commandMap.put("printwatches", "on|off");
        commandMap.put("quit", "");

    }


    public static void main(String[] args) {

        PrimeMinisterMain primeMinisterMain = new PrimeMinisterMain();
        try {
            primeMinisterMain.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    void run() throws IOException {


        System.out.println("Welcome to PrimeMinister!");

        boolean jlinemissing = false;
        // only use jline if it's in the classpath
        try {
            // 这一段,就是启用了命令行,控制台
            Class<?> consoleC = Class.forName("jline.console.ConsoleReader");
            Class<?> completorC =
                    Class.forName("com.chuhui.primeminister.JLineZNodeCompleter");

            System.out.println("JLine support is enabled");

            Object console =
                    consoleC.getConstructor().newInstance();

            Object completor =
                    completorC.newInstance();
            Method addCompletor = consoleC.getMethod("addCompleter",
                    Class.forName("jline.console.completer.Completer"));
            addCompletor.invoke(console, completor);

            String line;
            Method readLine = consoleC.getMethod("readLine", String.class);
            while ((line = (String) readLine.invoke(console, getPrompt())) != null) {
                executeLine(line);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            jlinemissing = true;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            jlinemissing = true;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            jlinemissing = true;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            jlinemissing = true;
        } catch (InstantiationException e) {
            e.printStackTrace();
            jlinemissing = true;
        }

        if (jlinemissing) {
            System.out.println("JLine support is disabled");
            BufferedReader br =
                    new BufferedReader(new InputStreamReader(System.in));

            String line;
            while ((line = br.readLine()) != null) {
                System.err.println(line);
            }
        }

        System.exit(1);
    }

    private String getPrompt() {
        // 前缀
        return "[pm:(localhost:chuhui) " + commandCount;
    }

    public void executeLine(String line) {
        if (!line.equals("")) {
            System.err.println("form terminal get data already," + line + " process");
            commandCount++;
        }
    }
    public static List<String> getCommands() {
        List<String> cmdList = new ArrayList<String>(commandMap.keySet());
        Collections.sort(cmdList);
        return cmdList;
    }


}
