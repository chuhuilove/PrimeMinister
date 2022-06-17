package com.chuhui.primeminister.collector.interceptor;

import com.chuhui.primeminister.collector.definition.TransformClassDefinition;
import com.chuhui.primeminister.collector.utils.CollectionsUtils;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * AbstractExtendingInterceptor
 *
 * @author: cyzi
 * @Date: 6/13/22
 * @Description:
 */
@Slf4j
public class AbstractExtendingInterceptor implements ExtendingInterceptor {
    @Override
    public void extendingFiled(TransformClassDefinition definition, CtClass originalCtClass) {
        List<Map.Entry<CtField, CtField.Initializer>> ctFields = extendedFields(definition, originalCtClass);

        if (CollectionsUtils.isNotEmpty(ctFields)) {
            for (Map.Entry<CtField, CtField.Initializer> extendedField : ctFields) {
                try {
                    originalCtClass.addField(extendedField.getKey(), extendedField.getValue());
                } catch (CannotCompileException e) {
                    log.error("add field failed!",e);
                }
            }
        }
    }

    @Override
    public void extendingBehaviors(TransformClassDefinition definition, CtClass originalCtClass) {

        List<CtBehavior> ctBehaviors = extendedBehaviors(definition, originalCtClass);

        if (CollectionsUtils.isNotEmpty(ctBehaviors)) {
            for (CtBehavior extendedBehavior : ctBehaviors) {

                try {
                    if (extendedBehavior.getMethodInfo().isMethod()) {
                        originalCtClass.addMethod((CtMethod) extendedBehavior);
                        continue;
                    }
                    if (extendedBehavior.getMethodInfo().isConstructor()) {
                        originalCtClass.addConstructor((CtConstructor) extendedBehavior);
                    }

                } catch (CannotCompileException e) {
                    log.error("add method failed!",e);
                }
            }
        }
    }

    @Override
    public void extendingInterface(TransformClassDefinition definition, CtClass originalCtClass) {
        List<CtClass> targetTypes = extendedInterface(definition, originalCtClass);

        if (CollectionsUtils.isNotEmpty(targetTypes)) {
            for (CtClass extendedType : targetTypes) {
                originalCtClass.addInterface(extendedType);
            }
        }

    }

    protected List<CtClass> extendedInterface(TransformClassDefinition definition, CtClass ctClass) {
        return null;
    }

    protected List<CtBehavior> extendedBehaviors(TransformClassDefinition definition, CtClass ctClass) {
        return null;
    }

    protected List<Map.Entry<CtField, CtField.Initializer>> extendedFields(TransformClassDefinition definition, CtClass ctClass) {
        return null;
    }

}
