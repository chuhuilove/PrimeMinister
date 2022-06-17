package com.chuhui.primeminister.collector;

import com.chuhui.primeminister.collector.config.PrimeMinisterPluginConfig;
import com.chuhui.primeminister.collector.interceptor.JVMLoadedClassEnhancePoint;
import com.chuhui.primeminister.collector.plugin.PrimeMinisterPluginSpecification;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;

/**
 * PrimeMinisterAgent
 *
 * @author: cyzi
 * @Date: 6/9/22
 * @Description:
 */
@Slf4j
public class PrimeMinisterAgent {

    public static void premain(String agentArgs, Instrumentation inst) {

        customizationJvmLoadedClass(inst);

        log.info("run customizationJvmLoadedClass completed, start PrimeMinister Agent");

        final List<PrimeMinisterPluginSpecification> supportedPlugins = loadSupportedPlugins();
        PrimeMinisterClassFileTransformer classFileTransformer = new PrimeMinisterClassFileTransformer(supportedPlugins);
        inst.addTransformer(classFileTransformer);
    }

    public static List<PrimeMinisterPluginSpecification> loadSupportedPlugins() {

        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        try {
            Enumeration<URL> fileResources = systemClassLoader.getResources(PrimeMinisterPluginConfig.PLUGIN_CONFIG_FILE_NAME);
            Set<PrimeMinisterPluginConfig> loadedPlugins = new HashSet<>();
            while (fileResources.hasMoreElements()) {
                URL url = fileResources.nextElement();
                PrimeMinisterPluginConfig loadedPlugin = PrimeMinisterPluginConfig.load(url);
                loadedPlugins.add(loadedPlugin);
            }
            return verifyAndInstantiation(loadedPlugins);
        } catch (IOException e) {
            log.error("load supported plugin failed!", e);
        }
        return null;
    }

    static List<PrimeMinisterPluginSpecification> verifyAndInstantiation(Set<PrimeMinisterPluginConfig> pluginDescs) {
        List<PrimeMinisterPluginSpecification> plugins = new LinkedList<>();
        for (PrimeMinisterPluginConfig desc : pluginDescs) {
            Set<String> definedPlugins = desc.getDefinedPlugins();
            for (String pluginClassName : definedPlugins) {
                try {
                    Class<?> clazz = Class.forName(pluginClassName);
                    if (PrimeMinisterPluginSpecification.class.isAssignableFrom(clazz)) {
                        plugins.add((PrimeMinisterPluginSpecification) clazz.newInstance());
                    }
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                    log.error("verify and instantiation plugin failed,plugin class name:{}.", pluginClassName, e);
                }
            }
        }
        return plugins;
    }


    public static void customizationJvmLoadedClass(final Instrumentation instrumentation) {

        Class[] loadedClasses = instrumentation.getAllLoadedClasses();
        ServiceLoader<JVMLoadedClassEnhancePoint> loadedEnhancePoints = ServiceLoader.load(JVMLoadedClassEnhancePoint.class);
        if (ArrayUtils.isNotEmpty(loadedClasses)) {

            for (Class clazz : loadedClasses) {
                loadedEnhancePoints.forEach(e -> e.enhanceLoadedClass(clazz, instrumentation));
            }
        }
    }


}
