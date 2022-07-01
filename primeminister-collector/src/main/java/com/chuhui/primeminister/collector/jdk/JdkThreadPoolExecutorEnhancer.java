package com.chuhui.primeminister.collector.jdk;

import ch.qos.logback.core.ConsoleAppender;
import com.chuhui.primeminister.collector.interceptor.JVMLoadedClassEnhancePoint;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * ThreadPoolExecutorEnhancer
 *
 * @author: cyzi
 * @Date: 6/24/22
 * @Description:
 */
@Slf4j
public class JdkThreadPoolExecutorEnhancer implements JVMLoadedClassEnhancePoint {


    private static final Map<String, String> PARAM_TYPE_NAME_TO_DECORATE_METHOD_CLASS = new HashMap<String, String>();
    private static final String RUNNABLE_CLASS_NAME = "java.lang.Runnable";

    {
        PARAM_TYPE_NAME_TO_DECORATE_METHOD_CLASS.put(RUNNABLE_CLASS_NAME, "com.alibaba.ttl.TtlRunnable");
        PARAM_TYPE_NAME_TO_DECORATE_METHOD_CLASS.put("java.util.concurrent.Callable", "com.alibaba.ttl.TtlCallable");

    }

    final Set<Class<?>> supportedClass = new HashSet<>();

    {
        supportedClass.add(ThreadPoolExecutor.class);
        supportedClass.add(ScheduledThreadPoolExecutor.class);

    }


    @Override
    public void enhanceLoadedClass(Class<?> clazz, Instrumentation instrumentation) {
        if (supportedClass.contains(clazz)) {


//            // 在提交任务的时候,把ThreadLocal中的数据提交进去
//
//            ClassPool classPool = ClassPool.getDefault();
//
//            CtClass ctClass = classPool.get(ConsoleAppender.class.getName());
//
//            CtMethod[] declaredMethods = ctClass.getDeclaredMethods();
//
//            for (CtMethod method : declaredMethods) {
//
//                int modifiers = method.getModifiers();
//                if (Modifier.isPublic(modifiers) && !Modifier.isStatic(modifiers)) {
//
//                    CtClass[] parameterTypes = method.getParameterTypes();
//                    StringBuilder insertCode = new StringBuilder();
//                    for (int i = 0; i < parameterTypes.length; i++) {
//                        final String paramTypeName = parameterTypes[i].getName();
//                        if (PARAM_TYPE_NAME_TO_DECORATE_METHOD_CLASS.containsKey(paramTypeName)) {
//                            String code = String.format(
//                                    "$%d = com.alibaba.ttl.threadpool.agent.internal.transformlet.impl.Utils.doAutoWrap($%<d);",
//                                    i + 1);
//                            insertCode.append(code);
//                        }
//                    }
//                    if (insertCode.length() > 0) {
//                        method.insertBefore(insertCode.toString());
//                    }
//                }
//            }
        }


    }

    @Override
    public int getPriority() {
        return 0;
    }
}
