package com.chuhui.primeminister.collector.plugin.ttl;

import com.chuhui.primeminister.collector.definition.TransformClassDefinition;
import com.chuhui.primeminister.collector.interceptor.EnhancePoint;
import com.chuhui.primeminister.collector.plugin.AbstractPrimeMinisterPluginSpecification;

import java.util.List;

/**
 * AlibabaTTLPlugins
 *
 * @author: cyzi
 * @Date: 6/13/22
 * @Description:
 */
public class AlibabaTTLPrimeMinisterPlugin extends AbstractPrimeMinisterPluginSpecification {
    //
    // 做兼容和适配,
    // 搞个




    @Override
    protected boolean matchPlugin(TransformClassDefinition classDefinition) {
        return false;
    }

    @Override
    public List<EnhancePoint> interceptorPoints() {
        return null;
    }
}
