package com.chuhui.primeminister.plugins.springmvc;

import ch.qos.logback.classic.Logger;
import com.chuhui.primeminister.collector.definition.TransformClassDefinition;
import com.chuhui.primeminister.collector.interceptor.EnhancePoint;
import com.chuhui.primeminister.collector.plugin.AbstractPrimeMinisterPluginSpecification;
import com.chuhui.primeminister.collector.utils.CollectionsUtils;
import javassist.CtClass;

import java.util.List;

/**
 * LoggingEventPrimeMinisterPlugin
 *
 * @author: cyzi
 * @Date: 6/15/22
 * @Description:
 */
public class LoggingEventPrimeMinisterPlugin extends AbstractPrimeMinisterPluginSpecification {


    private final Class<?> targetClazz= Logger.class;
    @Override
    protected boolean matchPlugin(TransformClassDefinition classDefinition) {

        return  false;
    }

    @Override
    public List<EnhancePoint> interceptorPoints() {
        return CollectionsUtils.newArrayList(new LoggingEventMethodConMethodInterceptor());
    }
}
