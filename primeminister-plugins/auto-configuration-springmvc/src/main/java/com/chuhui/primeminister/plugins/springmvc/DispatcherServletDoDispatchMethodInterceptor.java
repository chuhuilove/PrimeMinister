package com.chuhui.primeminister.plugins.springmvc;

import com.chuhui.primeminister.collector.definition.TransformClassDefinition;
import com.chuhui.primeminister.collector.interceptor.AbstractMethodInterceptor;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.CtMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * FeignPrimeMinisterPlugin
 *
 * @author: cyzi
 * @Date: 6/9/22
 * @Description:
 */
public class DispatcherServletDoDispatchMethodInterceptor extends AbstractMethodInterceptor {

    private  final String doDispatchMethod = "org.springframework.web.servlet.DispatcherServlet.doDispatch(javax.servlet.http.HttpServletRequest,javax.servlet.http.HttpServletResponse)";

    public static void doDispatchLogic(final HttpServletRequest servletRequest, final HttpServletResponse servletResponse) {
        System.err.println("servlet Request is:"+servletRequest);
        System.err.println("servlet Response is:"+servletResponse);
    }


    @Override
    protected boolean isSupported(final TransformClassDefinition definition, CtMethod method) {
        return doDispatchMethod.equals(method.getLongName());
    }

    @Override
    protected void setBehaviorLogic(TransformClassDefinition definition, CtBehavior behavior) {
        if(behavior instanceof CtMethod  && isSupported(definition,(CtMethod)behavior)){

            String beforeLogic = String.format(METHOD_LOGIC_TEMPLATE, "com.chuhui.primeminister.plugins.springmvc.DispatcherServletDoDispatchMethodInterceptor.doDispatchLogic($1,$2)");
            try {
                behavior.insertBefore(beforeLogic);
                System.err.println("插入的逻辑!!!!"+beforeLogic);
            } catch (CannotCompileException e) {
                e.printStackTrace();
            }
        }
    }
}
