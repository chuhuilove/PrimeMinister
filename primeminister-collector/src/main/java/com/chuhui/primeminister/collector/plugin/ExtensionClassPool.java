package com.chuhui.primeminister.collector.plugin;

import javassist.ClassPool;
import javassist.CtClass;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ExtensionClassPool
 *
 * @author: cyzi
 * @Date: 6/13/22
 * @Description:
 */
public class ExtensionClassPool extends ClassPool {

    private final static Map<ClassLoader, ExtensionClassPool> loadedClassMap = new ConcurrentHashMap<>(new WeakHashMap<>());
    private static final BootStrapClassLoader defaultBootStrapLoader = new BootStrapClassLoader();

    private static class BootStrapClassLoader extends ClassLoader {}


    public ExtensionClassPool() {
        super(true);
    }


    public static ExtensionClassPool getClassPool(ClassLoader loader) {
        ExtensionClassPool classPool = null;
        if (Objects.isNull(loader)) {
            // 默认的,引导类加载器
            classPool = loadedClassMap.get(defaultBootStrapLoader);
            loader = defaultBootStrapLoader;
        }

        if (Objects.isNull(classPool)) {
            // 使用的不是同一个ClassPool
            // 但是却是相同的系统路径classPath
            // 于是,同一个类,就可以被
            classPool = new ExtensionClassPool();
            // 除了类加载器不相同之外,里面的类都是相同的...
            // 相当于,对所有的类,都如此处理一遍,这么做,合适吗?
            // 有必要这么做吗?

            loadedClassMap.put(loader, classPool);
        }

        return classPool;
    }



    public CtClass makeCtClass(String className, byte[] classFileBuffer) {
        CtClass cachedCtClass = getCached(className);
        if (Objects.nonNull(cachedCtClass)) {
            // 这个缓存池里面,已经有了,而且还是同一个类加载器...
            // 不再重新创建CtClass了,
            return cachedCtClass;
        } else {
            try {

                return makeClass(new ByteArrayInputStream(classFileBuffer), false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public void removeCtClass(String className) {
        removeCached(className);
    }


}
