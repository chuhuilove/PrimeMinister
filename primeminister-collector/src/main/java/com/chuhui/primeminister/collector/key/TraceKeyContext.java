package com.chuhui.primeminister.collector.key;

import com.chuhui.primeminister.collector.definition.SimpleUUIDTraceKey;
import com.chuhui.primeminister.collector.definition.TraceKeyDefinition;

import java.util.Objects;

/**
 * TraceKeyContext
 *
 * @author: cyzi
 * @Date: 6/18/22
 * @Description:
 */
public class TraceKeyContext {

    private final static ThreadLocal<TraceKeyDefinition> globalTraceKey = new CyziThreadLocal<>();

    private TraceKeyContext() {
        throw new UnsupportedOperationException("TraceKeyContext forbid instantiation!");
    }

    public static TraceKeyDefinition getTraceKey() {
        return globalTraceKey.get();
    }

    public static TraceKeyDefinition getTraceKeyElsePut() {
        TraceKeyDefinition traceKeyDefinition = getTraceKey();
        if (Objects.isNull(traceKeyDefinition)) {
            traceKeyDefinition = new SimpleUUIDTraceKey();
        }
        putTraceKey(traceKeyDefinition);
        return traceKeyDefinition;
    }


    public static void putTraceKey(TraceKeyDefinition traceKeyDefinition) {
        globalTraceKey.set(traceKeyDefinition);
    }

    private static class CyziThreadLocal<T> extends InheritableThreadLocal<T> {

    }

}
