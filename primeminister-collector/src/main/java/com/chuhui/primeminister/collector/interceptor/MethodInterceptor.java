package com.chuhui.primeminister.collector.interceptor;

import com.chuhui.primeminister.collector.definition.TransformClassDefinition;
import javassist.CtBehavior;
import javassist.CtMethod;

import java.util.List;
import java.util.Set;

/**
 * MethodInterceptor
 *
 * @author: cyzi
 * @Date: 6/9/22
 * @Description:
 */
public interface MethodInterceptor extends EnhancePoint {

    List<CtBehavior> supportedBehavior(final TransformClassDefinition definition);

    void handleBehaviorLogic(final TransformClassDefinition definition, final List<CtBehavior> supportedBehaviors);

    @Override
    default int getPriority() {
        return Integer.MIN_VALUE + 2;
    }
}
