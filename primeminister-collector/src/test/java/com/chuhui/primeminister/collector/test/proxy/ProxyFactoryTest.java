package com.chuhui.primeminister.collector.test.proxy;

import com.chuhui.primeminister.collector.proxy.ProxyFactory;
import com.chuhui.primeminister.collector.proxy.TestInterface;
import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * ProxyFactoryTest
 *
 * @author: cyzi
 * @Date: 6/6/22
 * @Description:
 */
public class ProxyFactoryTest {


    @Test
    public void testClass(){
        ProxyFactory proxyFactory = ProxyFactory.getProxyFactory();
        TestClass1 proxy = (TestClass1) proxyFactory.createProxy(new TestClass1());
        System.err.println(proxy.getUUid());
        System.err.println(proxy.getUUid2());
    }


    @Test
    public void  testProxy(){
        TestInterface testInterface = (TestInterface) Proxy.newProxyInstance(TestInterface.class.getClassLoader(), new Class[]{TestInterface.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                if(method.getName().equals("getUUID")){
                    return UUID.randomUUID().toString();
                }

                return method.invoke(proxy,args);
            }
        });

        System.err.println( Proxy.isProxyClass(testInterface.getClass()));
        InvocationHandler invocationHandler = Proxy.getInvocationHandler(testInterface);
        System.err.println(invocationHandler);
        System.err.println(testInterface.getUUID());
        Class<?> proxyClass = Proxy.getProxyClass(TestInterface.class.getClassLoader(), TestInterface.class);

        // todo 明天测试一下,

        System.err.println(proxyClass);
    }



}
