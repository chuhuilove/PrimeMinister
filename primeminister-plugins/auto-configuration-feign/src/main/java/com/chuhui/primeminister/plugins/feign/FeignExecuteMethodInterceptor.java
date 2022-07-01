package com.chuhui.primeminister.plugins.feign;

import com.chuhui.primeminister.collector.definition.TraceKeyDefinition;
import com.chuhui.primeminister.collector.definition.TransformClassDefinition;
import com.chuhui.primeminister.collector.interceptor.AbstractMethodInterceptor;
import com.chuhui.primeminister.collector.key.TraceKeyContext;
import feign.Request;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.CtMethod;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;

import static com.chuhui.primeminister.collector.key.TraceKey.PRIME_MINISTER_COLLECTOR_UNIQUE_KEY;

/**
 * FeignPrimeMinisterPlugin
 *
 * @author: cyzi
 * @Date: 6/9/22
 * @Description:
 */
@Slf4j
public class FeignExecuteMethodInterceptor extends AbstractMethodInterceptor {


    String targetMethodLongName = "feign.Client$Default.execute(feign.Request,feign.Request$Options)";

    public static void feignExecuteRequestLogic(final Request convertedRequest) {

        TraceKeyDefinition traceKey = TraceKeyContext.getTraceKey();
        convertedRequest.headers()
                .put(PRIME_MINISTER_COLLECTOR_UNIQUE_KEY, Collections.singletonList(traceKey.createTraceKey()));

        log.info("feign carried trace key is:{}.",traceKey.createTraceKey());
    }


    @Override
    protected boolean isSupported(final TransformClassDefinition definition, CtMethod method) {
        return targetMethodLongName.equals(method.getLongName());
    }

    @Override
    protected void setBehaviorLogic(TransformClassDefinition definition, CtBehavior behavior) {
        if(behavior instanceof CtMethod  && isSupported(definition,(CtMethod)behavior)){

            String beforeLogic = String.format(METHOD_LOGIC_TEMPLATE, "com.chuhui.primeminister.plugins.feign.FeignExecuteMethodInterceptor.feignExecuteRequestLogic($1)");
            try {
                behavior.insertBefore(beforeLogic);
            } catch (CannotCompileException e) {
              log.error("weaving code error:{}.",beforeLogic,e);
            }
        }
    }
}
