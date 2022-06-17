package com.chuhui.primeminister.collector.logback;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.encoder.Encoder;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import com.chuhui.primeminister.collector.interceptor.JVMLoadedClassEnhancePoint;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * AppenderEnhancer
 *
 * @author: cyzi
 * @Date: 6/17/22
 * @Description:
 */
@Slf4j
public class AppenderEnhancer implements JVMLoadedClassEnhancePoint {

    static final String LOG_NAME_PREFIX = "com.chuhui.primeminister";

    static final String LOGGING_EVENT_FIELD_NAME = "message";


    final Set<Class<?>> supportedAppender = new HashSet<>();

    public AppenderEnhancer() {
        supportedAppender.add(AppenderBase.class);
        supportedAppender.add(UnsynchronizedAppenderBase.class);
    }

    @Override
    public void enhanceLoadedClass(Class<?> clazz, Instrumentation inst) {

        if (supportedAppender.contains(clazz)) {
            ClassPool defaultClassPool = ClassPool.getDefault();
            try {
                CtClass ctClass = defaultClassPool.get(clazz.getName());
                CtMethod doAppendMethod = ctClass.getDeclaredMethod("doAppend");
                doAppendMethod.insertBefore("{com.chuhui.primeminister.collector.logback.AppenderEnhancer.handleDoAppendMethod($1,this);}");

                byte[] bytes = ctClass.toBytecode();
                ClassDefinition classDefinition = new ClassDefinition(clazz, bytes);
                inst.redefineClasses(classDefinition);
            } catch (NotFoundException | CannotCompileException | IOException | ClassNotFoundException | UnmodifiableClassException e) {
                log.error("redefinition class:{} failed.", clazz, e);
            }
        }
    }




    public static void handleDoAppendMethod(final Object loggingEvent,final Object appender) {
        if (loggingEvent instanceof LoggingEvent  && appender instanceof ConsoleAppender) {
            LoggingEvent myLoggingEvent = (LoggingEvent) loggingEvent;
            String name = myLoggingEvent.getLoggerName();
            String originalMsg = myLoggingEvent.getMessage();
            try {
                if (name.startsWith(LOG_NAME_PREFIX)) {
                    Field message = LoggingEvent.class.getDeclaredField(LOGGING_EVENT_FIELD_NAME);
                    message.setAccessible(true);
                    message.set(myLoggingEvent, null);
                    myLoggingEvent.setMessage("prime-minister--->" + originalMsg);
                }

            } catch (NoSuchFieldException | IllegalAccessException e) {
                log.error("intercept method:{} failed.", LOGGING_EVENT_FIELD_NAME, e);
            }
        }
    }


    @Override
    public int getPriority() {
        return 0;
    }
}
