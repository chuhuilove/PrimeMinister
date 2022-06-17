package com.chuhui.primeminister.plugins.springmvc;

import com.chuhui.primeminister.collector.definition.TransformClassDefinition;
import com.chuhui.primeminister.collector.interceptor.AbstractMethodInterceptor;
import javassist.CtBehavior;
import javassist.CtConstructor;
import javassist.CtMethod;

/**
 * DispatcherConMethodInterceptor
 *
 * @author: cyzi
 * @Date: 6/15/22
 * @Description:
 */
public class LoggingEventMethodConMethodInterceptor extends AbstractMethodInterceptor {



    // 一个是构造器
    // 一个是set方法


    @Override
    protected boolean isSupported(final TransformClassDefinition definition, CtMethod method) {

        System.err.println(method.getLongName());
        return false;
    }

    @Override
    protected boolean isSupported(final TransformClassDefinition definition, CtConstructor constructor) {
        // 需要子类去实现
        System.err.println(constructor.getLongName());
        return false;
    }


    @Override
    protected void setBehaviorLogic(TransformClassDefinition definition, CtBehavior behavior) {

    }
}
