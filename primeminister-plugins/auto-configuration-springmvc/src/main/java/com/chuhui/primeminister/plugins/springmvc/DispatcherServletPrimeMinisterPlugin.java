package com.chuhui.primeminister.plugins.springmvc;

import com.chuhui.primeminister.collector.ClassDescInfoHolder;
import com.chuhui.primeminister.collector.key.TraceKey;
import com.chuhui.primeminister.collector.plugin.AbstractPrimeMinisterPlugin;
import javassist.CtMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * FrameworkServlet
 *
 * @author: cyzi
 * @Date: 6/10/22
 * @Description:
 */
public class DispatcherServletPrimeMinisterPlugin extends AbstractPrimeMinisterPlugin {

    private String supportedClassName = "org.springframework.web.servlet.DispatcherServlet";


    private String doDispatchMethod = "org.springframework.web.servlet.DispatcherServlet.doDispatch";


    @Override
    public boolean isSupported(ClassDescInfoHolder classDescInfoHolder) {
        return supportedClassName.equals(classDescInfoHolder.getClassName());
    }

    @Override
    protected boolean hasBefore(CtMethod method) {
        return false;
    }

    @Override
    protected String beforeLogic(CtMethod method) {
        return null;
    }

    @Override
    protected boolean hasAfter(CtMethod method) {
        return false;
    }

    @Override
    protected String afterLogic(CtMethod method) {
        return null;
    }

    public static void handleRequest(final HttpServletRequest request) {

        //  这是有请求进来了
        Enumeration headers = request.getHeaders(TraceKey.PRIME_MINISTER_COLLECTOR_UNIQUE_KEY);
        while (headers.hasMoreElements()) {

        }

    }

}
