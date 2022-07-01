package com.chuhui.primeminister.collector.logback;

import ch.qos.logback.core.ConsoleAppender;
import com.chuhui.primeminister.collector.interceptor.JVMLoadedClassEnhancePoint;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

/**
 * ConsoleAppenderEnhancer
 * 增强控制台日志,算是废了
 * @author: cyzi
 * @Date: 6/17/22
 * @Description:
 */
@Slf4j
public class ConsoleAppenderEnhancer implements JVMLoadedClassEnhancePoint {

    @Override
    public void enhanceLoadedClass(Class<?> clazz, Instrumentation inst) {

        if (ConsoleAppender.class.equals(clazz)) {

            ClassPool classPool = ClassPool.getDefault();
            try {
                CtClass ctClass = classPool.get(ConsoleAppender.class.getName());
                CtConstructor[] consoleAppenderConstructors = ctClass.getDeclaredConstructors();
                CtConstructor consoleAppenderConstructor = consoleAppenderConstructors[0];
                consoleAppenderConstructor.setBody("{super();    this.target = ch.qos.logback.core.joran.spi.ConsoleTarget.SystemOut; this.withJansi = false;   com.chuhui.primeminister.collector.logback.ConsoleAppenderEnhancer.createConsoleAppender();}");

                byte[] bytes = ctClass.toBytecode();
                ClassDefinition classDefinition = new ClassDefinition(clazz, bytes);
                inst.redefineClasses(classDefinition);
            } catch (NotFoundException | CannotCompileException | IOException | ClassNotFoundException | UnmodifiableClassException e) {
                log.error("redefinition  class:{} failed.", clazz, e);
            }
        }

    }


    public static void createConsoleAppender(){

    }


    @Override
    public int getPriority() {
        return 0;
    }
}
