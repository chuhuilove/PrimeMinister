package com.chuhui.primeminister.collector.definition;

import com.chuhui.primeminister.collector.ClassDescInfoHolder;
import javassist.CtClass;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;

/**
 * TransClassDesc
 *
 * @author: cyzi
 * @Date: 6/13/22
 * @Description:
 */
public final class TransformClassDefinition {

    private static final Map<TransformClassDefinitionKey, TransformClassDefinition> transformClassDefinitionMap = Collections.synchronizedMap(new WeakHashMap<>());

    private final ClassDescInfoHolder originalClassInfoHolder;
    private final CtClass ctClass;

    public TransformClassDefinition(ClassDescInfoHolder classDescInfoHolder, CtClass ctClass) {
        this.originalClassInfoHolder = classDescInfoHolder;
        this.ctClass = ctClass;
        transformClassDefinitionMap.put(new TransformClassDefinitionKey(classDescInfoHolder.getLoader(), classDescInfoHolder.getClassName()), this);
    }


    public static TransformClassDefinition getTransformClassDefinition(ClassLoader loader, String className) {
        TransformClassDefinitionKey key = new TransformClassDefinitionKey(loader, className);
        return transformClassDefinitionMap.get(key);
    }


    public ClassDescInfoHolder getOriginalClassInfoHolder() {
        return originalClassInfoHolder;
    }

    public CtClass getCtClass() {
        return ctClass;
    }

    private static class TransformClassDefinitionKey {
        private final ClassLoader loader;
        private final String className;

        TransformClassDefinitionKey(ClassLoader loader, String className) {
            this.className = className;
            this.loader = loader;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            TransformClassDefinitionKey that = (TransformClassDefinitionKey) o;
            return loader.equals(that.loader) &&
                    className.equals(that.className);
        }

        @Override
        public int hashCode() {
            return Objects.hash(loader, className);
        }
    }
}
