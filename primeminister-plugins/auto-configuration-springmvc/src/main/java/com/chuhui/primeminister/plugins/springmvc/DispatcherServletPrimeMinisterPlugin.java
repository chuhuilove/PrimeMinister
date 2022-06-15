package com.chuhui.primeminister.plugins.springmvc;

import com.chuhui.primeminister.collector.definition.TransformClassDefinition;
import com.chuhui.primeminister.collector.interceptor.EnhancePoint;
import com.chuhui.primeminister.collector.plugin.AbstractPrimeMinisterPluginSpecification;
import com.chuhui.primeminister.collector.utils.CollectionsUtils;

import java.util.List;

/**
 * FrameworkServlet
 *
 * @author: cyzi
 * @Date: 6/10/22
 * @Description:
 */
public class DispatcherServletPrimeMinisterPlugin extends AbstractPrimeMinisterPluginSpecification {

    private String supportedClassName = "org.springframework.web.servlet.DispatcherServlet";




    @Override
    public List<EnhancePoint> interceptorPoints() {
        return CollectionsUtils.newArrayList(new DispatcherServletDoDispatchMethodInterceptor());
    }

    @Override
    protected boolean matchPlugin(TransformClassDefinition classDefinition) {
        return supportedClassName.equals(classDefinition.getOriginalClassInfoHolder().getClassName());
    }



}
