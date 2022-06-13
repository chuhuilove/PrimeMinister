package com.chuhui.primeminister.collector.interceptor;

import com.chuhui.primeminister.collector.definition.TransformClassDefinition;
import javassist.CtBehavior;
import javassist.CtClass;

import java.util.List;

/**
 * FieldInterceptor
 *
 * @author: cyzi
 * @Date: 6/9/22
 * @Description:
 */
public interface ExtendingInterceptor extends EnhancePoint {

    void extendingFiled(final TransformClassDefinition definition, CtClass ctClass);

    void extendingBehaviors(final TransformClassDefinition definition, CtClass ctClass);

    void extendingInterface(final TransformClassDefinition definition, CtClass ctClass);

    @Override
    default int getPriority() {
        return Integer.MIN_VALUE;
    }
}
