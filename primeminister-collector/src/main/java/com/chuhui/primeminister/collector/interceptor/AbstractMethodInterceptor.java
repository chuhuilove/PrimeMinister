package com.chuhui.primeminister.collector.interceptor;

import com.chuhui.primeminister.collector.definition.TransformClassDefinition;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * AbstractMethodInterceptor
 *
 * @author: cyzi
 * @Date: 6/9/22
 * @Description:
 */
@Slf4j
public abstract class AbstractMethodInterceptor implements MethodInterceptor {

    protected final static String METHOD_LOGIC_TEMPLATE = "{ try{ %s; }catch(Exception e){ com.chuhui.primeminister.collector.interceptor.AbstractMethodInterceptor.handleException(e);} }";

    @Override
    public List<CtBehavior> supportedBehavior(final TransformClassDefinition definition) {


        CtClass ctClass = definition.getCtClass();
        CtBehavior[] allBehaviors = ctClass.getDeclaredBehaviors();

        final List<CtBehavior> supportedBehaviors = new LinkedList<>();
        if (Objects.nonNull(allBehaviors) && allBehaviors.length > 0) {
            for (CtBehavior behavior : allBehaviors) {

                if (behavior.getMethodInfo().isMethod() && isSupported(definition, (CtMethod) behavior)) {
                    supportedBehaviors.add(behavior);
                    continue;
                }

                // todo 这个地方怎么直接去调用构造器了呢?
                if (behavior.getMethodInfo().isConstructor() && isSupported(definition, (CtConstructor) behavior)) {
                    supportedBehaviors.add(behavior);
                }
            }
        }
        return supportedBehaviors;
    }

    @Override
    public void handleBehaviorLogic(final TransformClassDefinition definition, final List<CtBehavior> supportedBehaviors) {

        for (CtBehavior ctBehavior : supportedBehaviors) {
            setBehaviorLogic(definition, ctBehavior);
        }
    }


    protected boolean isSupported(final TransformClassDefinition definition, CtMethod method) {
        // 需要子类去实现
        return false;
    }

    protected boolean isSupported(final TransformClassDefinition definition, CtConstructor constructor) {
        // 需要子类去实现
        return false;
    }

    protected abstract void setBehaviorLogic(final TransformClassDefinition definition, CtBehavior behavior);


    public static void handleException(final Throwable throwable) {
          log.error("invoke customization logic error.",throwable);
    }

}
