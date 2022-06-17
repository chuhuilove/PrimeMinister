package com.chuhui.primeminister.collector.interceptor;

import java.lang.instrument.Instrumentation;

/**
 * JVMLoadedClassEnhancePoint
 *
 * @author: cyzi
 * @Date: 6/17/22
 * @Description:
 */
public interface JVMLoadedClassEnhancePoint extends EnhancePoint {

    void enhanceLoadedClass(Class<?> clazz, Instrumentation instrumentation);

}
