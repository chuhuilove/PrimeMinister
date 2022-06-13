package com.chuhui.primeminister.plugins.feign;

import com.chuhui.primeminister.collector.definition.TransformClassDefinition;
import com.chuhui.primeminister.collector.interceptor.EnhancePoint;
import com.chuhui.primeminister.collector.plugin.AbstractPrimeMinisterPluginSpecification;
import com.chuhui.primeminister.collector.utils.CollectionsUtils;

import java.util.List;

/**
 * FeignPrimeMinisterPlugin
 *
 * @author: cyzi
 * @Date: 6/9/22
 * @Description:
 */
public class FeignClientDefaultPrimeMinisterPlugin extends AbstractPrimeMinisterPluginSpecification {

    String supportedClassName = "feign.Client$Default";

    @Override
    protected boolean matchPlugin(TransformClassDefinition classDefinition) {
        return supportedClassName.equals(classDefinition.getOriginalClassInfoHolder().getClassName());
    }

    @Override
    public List<EnhancePoint> interceptorPoints() {
        return CollectionsUtils.newArrayList(new FeignExecuteMethodInterceptor());
    }

}
