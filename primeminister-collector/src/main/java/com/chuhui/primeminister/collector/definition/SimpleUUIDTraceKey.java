package com.chuhui.primeminister.collector.definition;

import java.util.UUID;

/**
 * TraceKeyDefinition
 *
 * @author: cyzi
 * @Date: 6/10/22
 * @Description:
 */
public class SimpleUUIDTraceKey implements TraceKeyDefinition {


    private final String traceKey;

    public SimpleUUIDTraceKey() {
        this(UUID.randomUUID().toString().replaceAll("-", ""));
    }

    public SimpleUUIDTraceKey(String specifiedTraceKey) {
        this.traceKey = specifiedTraceKey;
    }

    @Override
    public String createTraceKey() {
        return traceKey;
    }
}
