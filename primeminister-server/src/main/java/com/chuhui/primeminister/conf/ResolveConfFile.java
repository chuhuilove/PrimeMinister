package com.chuhui.primeminister.conf;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;

/**
 * ResolveConfFile
 *
 * 解析配置文件
 *
 * 非线程安全的单例
 *
 * @author: cyzi
 * @Date: 2019/9/2 0002
 * @Description:TODO
 */
public class ResolveConfFile {


    private static ResolveConfFile instance;

    public static ResolveConfFile getInstance() {
        if (instance == null) {
            instance = new ResolveConfFile();
        }
        return instance;
    }

    private ResolveConfFile() {



    }

}
