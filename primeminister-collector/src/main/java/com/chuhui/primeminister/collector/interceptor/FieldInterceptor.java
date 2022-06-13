package com.chuhui.primeminister.collector.interceptor;

/**
 * FieldInterceptor
 *
 * @author: cyzi
 * @Date: 6/9/22
 * @Description:
 */
public interface FieldInterceptor extends EnhancePoint {


    @Override
    default int getPriority() {
        return Integer.MIN_VALUE+1;
    }
}
