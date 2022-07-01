package com.chuhui.primeminister.plugins.springmvc;

import com.chuhui.primeminister.collector.definition.SimpleUUIDTraceKey;
import com.chuhui.primeminister.collector.definition.TraceKeyDefinition;
import com.chuhui.primeminister.collector.definition.TransformClassDefinition;
import com.chuhui.primeminister.collector.interceptor.AbstractMethodInterceptor;
import com.chuhui.primeminister.collector.key.TraceKeyContext;
import com.chuhui.primeminister.collector.utils.StringUtils;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.CtMethod;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashSet;
import java.util.Set;

import static com.chuhui.primeminister.collector.key.TraceKey.PRIME_MINISTER_COLLECTOR_UNIQUE_KEY;

/**
 * FeignPrimeMinisterPlugin
 *
 * @author: cyzi
 * @Date: 6/9/22
 * @Description:
 */
@Slf4j
public class DispatcherServletDoDispatchMethodInterceptor extends AbstractMethodInterceptor {


    private final String doDispatchMethod = "org.springframework.web.servlet.DispatcherServlet.doDispatch(javax.servlet.http.HttpServletRequest,javax.servlet.http.HttpServletResponse)";

    public static void doDispatchLogic(final HttpServletRequest servletRequest, final HttpServletResponse servletResponse) {

        if (filterUrl(servletRequest)) {
            return;
        }

        String header = servletRequest.getHeader(PRIME_MINISTER_COLLECTOR_UNIQUE_KEY);
        TraceKeyDefinition traceKey = null;

        if (StringUtils.isEmpty(header)) {
            // 表示,需要重新生成
            traceKey = new SimpleUUIDTraceKey();
            log.info("create new  trace key is:{}.", traceKey.createTraceKey());
        } else {
            traceKey = new SimpleUUIDTraceKey(header);
            log.info("Http Servlet Request tract key is:{}.", traceKey.createTraceKey());
        }
        TraceKeyContext.putTraceKey(traceKey);
    }

    @Override
    protected boolean isSupported(final TransformClassDefinition definition, CtMethod method) {
        return doDispatchMethod.equals(method.getLongName());
    }

    @Override
    protected void setBehaviorLogic(TransformClassDefinition definition, CtBehavior behavior) {
        if (behavior instanceof CtMethod && isSupported(definition, (CtMethod) behavior)) {

            String beforeLogic = String.format(METHOD_LOGIC_TEMPLATE, "com.chuhui.primeminister.plugins.springmvc.DispatcherServletDoDispatchMethodInterceptor.doDispatchLogic($1,$2)");
            try {
                behavior.insertBefore(beforeLogic);
            } catch (CannotCompileException e) {
                log.error("compiler logic is:{}.", beforeLogic, e);
            }
        }
    }

    private final static Set<String> urls = new HashSet<>();

    {
        urls.add("health");
    }

    protected static boolean filterUrl(HttpServletRequest servletRequest) {

        String requestURI = servletRequest.getRequestURI();

        for (String url : urls) {
            if (requestURI.startsWith(url) || requestURI.endsWith(url)) {
                return true;
            }
        }
        return false;
    }

}
