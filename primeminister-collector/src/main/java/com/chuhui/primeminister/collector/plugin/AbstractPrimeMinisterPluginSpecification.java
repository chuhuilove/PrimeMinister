package com.chuhui.primeminister.collector.plugin;

import com.chuhui.primeminister.collector.ClassDescInfoHolder;
import com.chuhui.primeminister.collector.definition.TransformClassDefinition;
import com.chuhui.primeminister.collector.interceptor.EnhancePoint;
import com.chuhui.primeminister.collector.interceptor.ExtendingInterceptor;
import com.chuhui.primeminister.collector.interceptor.MethodInterceptor;
import com.chuhui.primeminister.collector.utils.CollectionsUtils;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.CtClass;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static com.chuhui.primeminister.collector.definition.TransformClassDefinition.getTransformClassDefinition;

/**
 * PrimeMinisterPlugin
 *
 * @author: cyzi
 * @Date: 6/11/22
 * @Description:
 */
public abstract class AbstractPrimeMinisterPluginSpecification implements PrimeMinisterPluginSpecification {


    @Override
    public boolean isSupported(final ClassDescInfoHolder classDescInfoHolder) {

        ClassLoader loader = classDescInfoHolder.getLoader();
        ExtensionClassPool classPool = ExtensionClassPool.getClassPool(loader);
        CtClass ctClass = classPool.makeCtClass(classDescInfoHolder.getClassName(), classDescInfoHolder.getClassfileBuffer());

        if (Objects.isNull(ctClass)) {
            //  没能获取到CtClass对象
            return false;
        }

        boolean matched = matchPlugin(new TransformClassDefinition(classDescInfoHolder, ctClass));
        if (!matched) {
            classPool.removeCtClass(ctClass.getName());
        }
        return matched;
    }

    @Override
    public byte[] customizationByteCode(final ClassDescInfoHolder classDescInfoHolder) {
        TransformClassDefinition transformClassDefinition = getTransformClassDefinition(classDescInfoHolder.getLoader(), classDescInfoHolder.getClassName());
        if (Objects.isNull(transformClassDefinition)) {
            throw new NullPointerException("TransformClassDefinition不允许为空");
        }

        CtClass ctClass = transformClassDefinition.getCtClass();
        if (ctClass.isFrozen()) {
            ctClass.defrost();
        }
        customizationLogic(transformClassDefinition);
        try {
            return transformClassDefinition.getCtClass().toBytecode();
        } catch (IOException | CannotCompileException e) {
            e.printStackTrace();
        }

        return null;
    }


    protected abstract boolean matchPlugin(final TransformClassDefinition classDefinition);

    protected void customizationLogic(final TransformClassDefinition transformClassDefinition) {
        List<EnhancePoint> interceptorPoints = interceptorPoints();
        if (CollectionsUtils.isEmpty(interceptorPoints)) {
            throw new NullPointerException("增强点为空");
        }

        interceptorPoints.sort(Comparator.comparing(EnhancePoint::getPriority));

        for (EnhancePoint interceptorPoint : interceptorPoints) {

            if (interceptorPoint instanceof ExtendingInterceptor) {
                CtClass ctClass = transformClassDefinition.getCtClass();
                ExtendingInterceptor extendingInterceptor = (ExtendingInterceptor) interceptorPoint;
                // 先扩展接口
                extendingInterceptor.extendingInterface(transformClassDefinition, ctClass);
                // 再扩展字段
                extendingInterceptor.extendingFiled(transformClassDefinition, ctClass);
                // 最后扩展方法
                extendingInterceptor.extendingBehaviors(transformClassDefinition, ctClass);
            }


            if (interceptorPoint instanceof MethodInterceptor) {
                MethodInterceptor methodInterceptor = (MethodInterceptor) interceptorPoint;
                List<CtBehavior> ctBehaviors = methodInterceptor.supportedBehavior(transformClassDefinition);
                if (CollectionsUtils.isNotEmpty(ctBehaviors)) {
                    methodInterceptor.handleBehaviorLogic(transformClassDefinition, ctBehaviors);
                }
            }
        }

    }
}
