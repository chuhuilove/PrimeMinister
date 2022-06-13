package com.chuhui.primeminister.collector.plugin;

import com.chuhui.primeminister.collector.ClassDescInfoHolder;
import com.chuhui.primeminister.collector.interceptor.EnhancePoint;

import java.util.List;

/**
 * PrimeMinisterPluginSpecification
 * 同一个类加载器对象,不允许二次加载
 * @author: cyzi
 * @Date: 6/11/22
 * @Description:
 */
public interface PrimeMinisterPluginSpecification {

    /**
     *  给定的类,是否支持修改字节码
     * @param classDescInfoHolder
     * @return
     */
    boolean isSupported(final ClassDescInfoHolder classDescInfoHolder);

    /**
     * 定制自己的字节码
     * 当,一个类的字节码已经被修改以后,允不允许二次更改呢?
     *
     * 允许.
     * @param classDescInfoHolder
     * @return
     */
    byte [] customizationByteCode(final ClassDescInfoHolder classDescInfoHolder);


    /**
     * 插件扩展点
     * @return
     */
    List<EnhancePoint> interceptorPoints();


}
