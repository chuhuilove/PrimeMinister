package com.chuhui.primeminister.collector.config;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Set;

/**
 * PrimeMinisterPluginDesc
 *
 * @author: cyzi
 * @Date: 6/9/22
 * @Description:
 */
final public class PrimeMinisterPluginConfig {

    /**
     * 插件配置文件的名称
     */
    public final static String PLUGIN_CONFIG_FILE_NAME = "prime-minister-plugin.yml";

    private final String pluginName;

    private final Set<String> definedPlugins;

    public PrimeMinisterPluginConfig(String pluginName, Set<String> definedPlugins) {
        this.pluginName = pluginName;
        this.definedPlugins = definedPlugins;
    }

    public PrimeMinisterPluginConfig(PrimeMinisterPluginDescTemp descTemp) {
        this(descTemp.getPluginName(), descTemp.getDefinedPlugins());
    }


    public String getPluginName() {
        return pluginName;
    }

    public Set<String> getDefinedPlugins() {
        return definedPlugins;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PrimeMinisterPluginConfig that = (PrimeMinisterPluginConfig) o;
        return pluginName.equals(that.pluginName) &&
                definedPlugins.equals(that.definedPlugins);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pluginName, definedPlugins);
    }

    public static PrimeMinisterPluginConfig load(URL url) throws IOException {
        Yaml yaml = new Yaml();
        PrimeMinisterPluginDescTemp loadedPlugin = yaml.loadAs(url.openStream(), PrimeMinisterPluginDescTemp.class);
        return new PrimeMinisterPluginConfig(loadedPlugin);
    }

    public static class PrimeMinisterPluginDescTemp {
        private String pluginName;
        private Set<String> definedPlugins;

        public String getPluginName() {
            return pluginName;
        }

        public void setPluginName(String pluginName) {
            this.pluginName = pluginName;
        }

        public Set<String> getDefinedPlugins() {
            return definedPlugins;
        }

        public void setDefinedPlugins(Set<String> definedPlugins) {
            this.definedPlugins = definedPlugins;
        }
    }


}
