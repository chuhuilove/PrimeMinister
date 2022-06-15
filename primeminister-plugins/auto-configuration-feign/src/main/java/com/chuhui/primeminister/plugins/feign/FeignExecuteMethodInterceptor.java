package com.chuhui.primeminister.plugins.feign;

import com.chuhui.primeminister.collector.definition.TransformClassDefinition;
import com.chuhui.primeminister.collector.interceptor.AbstractMethodInterceptor;
import com.chuhui.primeminister.collector.key.TraceKey;
import com.chuhui.primeminister.collector.plugin.AbstractPrimeMinisterPluginSpecification;
import feign.Request;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.CtMethod;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * FeignPrimeMinisterPlugin
 *
 * @author: cyzi
 * @Date: 6/9/22
 * @Description:
 */
public class FeignExecuteMethodInterceptor extends AbstractMethodInterceptor {


    String targetMethodLongName = "feign.Client$Default.execute(feign.Request,feign.Request$Options)";

    public static void feignExecuteRequestLogic(final Request convertedRequest) {

        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        Map<String, Collection<String>> headers = convertedRequest.headers();
        List<String> headerValues = new ArrayList<>();
        headerValues.add(uuid);
        System.err.println("本次调用,生成的uuid:" + uuid + ",thread is:" + Thread.currentThread().getName());
        headers.put(TraceKey.PRIME_MINISTER_COLLECTOR_UNIQUE_KEY, headerValues);
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
                System.err.println("插入的逻辑!!!!"+beforeLogic);
            } catch (CannotCompileException e) {
                e.printStackTrace();
            }
        }
    }
}
