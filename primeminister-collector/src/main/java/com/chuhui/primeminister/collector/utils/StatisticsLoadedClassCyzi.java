package com.chuhui.primeminister.collector.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static com.chuhui.primeminister.collector.utils.StatisticsLoadedClassCyzi.ClassLoaderType.BOOTSTRAP_CLASSLOADER;
import static com.chuhui.primeminister.collector.utils.StatisticsLoadedClassCyzi.ClassLoaderType.EXTENSION_CLASSLOADER;
import static com.chuhui.primeminister.collector.utils.StatisticsLoadedClassCyzi.ClassLoaderType.SYSTEM_CLASSLOADER;

/**
 * LoadedClassStatic
 *
 * @author: cyzi
 * @Date: 6/9/22
 * @Description:
 */
public class StatisticsLoadedClassCyzi {


    enum ClassLoaderType {
        BOOTSTRAP_CLASSLOADER,
        EXTENSION_CLASSLOADER,
        SYSTEM_CLASSLOADER
    }


    static Map<ClassLoaderType, Set<String>> loadedClassNames = new HashMap<>();

    static {
        System.err.println("StatisticsLoadedClassCyzi的类加载器:"+StatisticsLoadedClassCyzi.class.getClassLoader());
        loadedClassNames.put(BOOTSTRAP_CLASSLOADER, Collections.synchronizedSet(new HashSet<>()));
        loadedClassNames.put(EXTENSION_CLASSLOADER, Collections.synchronizedSet(new HashSet<>()));
        loadedClassNames.put(SYSTEM_CLASSLOADER, Collections.synchronizedSet(new HashSet<>()));
    }

    private StatisticsLoadedClassCyzi() {
    }

    public static void putLoadedClass(ClassLoader loader, String className) {
        if (Objects.isNull(loader)) {
            // 第一层,引导类加载器
            loadedClassNames.get(BOOTSTRAP_CLASSLOADER).add(className);
          //  System.err.println("引导类加载器:"+className);
        } else {
            ClassLoader parent = loader.getParent();
            if (Objects.isNull(parent)) {
                // 第二层 扩展类加载器
                loadedClassNames.get(EXTENSION_CLASSLOADER).add(className);
              //  System.err.println(loader+"扩展类加载器:"+className);
            } else {
                // 第三层,系统类加载器
                loadedClassNames.get(SYSTEM_CLASSLOADER).add(className);
              //  System.err.println(loader+"系统类加载器:"+className);
            }
        }
    }


    public  static void print(){
        loadedClassNames.forEach((key,value)->{
            System.err.println("==================="+key+"=======================");
            for(String loadedClassName:value){
                System.err.println(key+"加载的类:"+loadedClassName);
            }
        });
    }
}
