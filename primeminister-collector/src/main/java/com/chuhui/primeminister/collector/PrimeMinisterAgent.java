package com.chuhui.primeminister.collector;

import com.chuhui.primeminister.collector.plugin.AbstractPrimeMinisterPlugin;
import com.chuhui.primeminister.collector.plugin.PrimeMinisterPluginDesc;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * PrimeMinisterAgent
 *
 * @author: cyzi
 * @Date: 6/9/22
 * @Description:
 */
public class PrimeMinisterAgent {

    public static void premain(String agentArgs, Instrumentation inst) {

        final List<AbstractPrimeMinisterPlugin> supportedPlugins = loadSupportedPlugins();
        inst.addTransformer(new PrimeMinisterClassFileTransformer(supportedPlugins),true);
    }

    public static List<AbstractPrimeMinisterPlugin> loadSupportedPlugins() {

        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        try {
            Enumeration<URL> fileResources = systemClassLoader.getResources(PrimeMinisterPluginDesc.PLUGIN_CONFIG_FILE_NAME);
            Set<PrimeMinisterPluginDesc> loadedPlugins = new HashSet<>();
            while (fileResources.hasMoreElements()) {
                URL url = fileResources.nextElement();
                PrimeMinisterPluginDesc loadedPlugin = PrimeMinisterPluginDesc.load(url);
                loadedPlugins.add(loadedPlugin);
            }
            return verifyAndInstantiation(loadedPlugins);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    static List<AbstractPrimeMinisterPlugin> verifyAndInstantiation(Set<PrimeMinisterPluginDesc> pluginDescs) {
        List<AbstractPrimeMinisterPlugin> plugins = new LinkedList<>();
        for (PrimeMinisterPluginDesc desc : pluginDescs) {
            Set<String> definedPlugins = desc.getDefinedPlugins();
            for (String pluginClassName : definedPlugins) {
                try {
                    Class<?> clazz = Class.forName(pluginClassName);
                    if (AbstractPrimeMinisterPlugin.class.isAssignableFrom(clazz)) {
                        plugins.add((AbstractPrimeMinisterPlugin) clazz.newInstance());
                    }
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return plugins;
    }
}