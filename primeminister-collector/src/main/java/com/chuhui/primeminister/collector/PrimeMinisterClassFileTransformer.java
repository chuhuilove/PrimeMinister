package com.chuhui.primeminister.collector;

import com.chuhui.primeminister.collector.plugin.AbstractPrimeMinisterPlugin;
import com.chuhui.primeminister.collector.plugin.PrimeMinisterPluginSpecification;
import com.chuhui.primeminister.collector.utils.CollectionsUtils;
import com.chuhui.primeminister.collector.utils.StatisticsLoadedClassCyzi;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * PrimeMinisterClassFileTransformer
 *
 * @author: cyzi
 * @Date: 6/9/22
 * @Description:
 */
public class PrimeMinisterClassFileTransformer implements ClassFileTransformer {

    private final byte[] NO_TRANSFORMED=null;
    private final List<AbstractPrimeMinisterPlugin> supportedPlugins;
    private final List<PrimeMinisterPluginSpecification> supportedPluginSpecifications;

    public PrimeMinisterClassFileTransformer(List<AbstractPrimeMinisterPlugin> supportedPlugins) {
        this.supportedPlugins = supportedPlugins;
        supportedPluginSpecifications=new LinkedList<>();
    }


    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

        // 如果loader为空,则表明是引导类加载器加载的类
        StatisticsLoadedClassCyzi.putLoadedClass(loader, className);


        if (CollectionsUtils.isNotEmpty(supportedPluginSpecifications)) {
            ClassDescInfoHolder classHolder = new ClassDescInfoHolder(className, loader, classBeingRedefined, classfileBuffer);
            for (PrimeMinisterPluginSpecification plugin : supportedPluginSpecifications) {
                if (plugin.isSupported(classHolder)) {
                    // 一个class只允许织入一次
                    System.err.println("本次允许的类:" + classHolder);

                    return plugin.customizationByteCode(classHolder);
                }
            }
        }

        /**
         * 设计哲学是什么?
         * 屏蔽掉插件实现者对底层Javassist的感知
         *
         *
         */

        return null;
    }
}
