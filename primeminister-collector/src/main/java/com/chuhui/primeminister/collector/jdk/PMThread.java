package com.chuhui.primeminister.collector.jdk;

import com.chuhui.primeminister.collector.definition.TraceKeyDefinition;
import com.chuhui.primeminister.collector.key.TraceKeyContext;

/**
 * PMRunnable
 *
 * @author: cyzi
 * @Date: 6/30/22
 * @Description:
 */
public class PMThread implements Runnable, PMRunnable {

    private final Runnable actualCommittedTask;

    private final TraceKeyDefinition holdTraceKey;

    public PMThread(Runnable task){
        this.actualCommittedTask=task;
        holdTraceKey = TraceKeyContext.getTraceKey();
    }

    @Override
    public void run() {
        actualCommittedTask.run();
    }
}
