package com.chuhui.primeminister.collector;

import com.chuhui.primeminister.collector.network.PrimeMinisterNetworkClient;

import java.util.Arrays;

/**
 * ClassDescInfo
 *
 * @author: cyzi
 * @Date: 6/9/22
 * @Description:
 */
public class ClassDescInfoHolder {

    private final ClassLoader loader;

    private final String vmClassName;

    private final String className;

    private final Class<?> classBeingRedefined;

    private final byte[] classfileBuffer;

    private final PrimeMinisterNetworkClient networkClient;

    public ClassDescInfoHolder(String vmClassName, ClassLoader loader, Class<?> classBeingRedefined, byte[] classfileBuffer,PrimeMinisterNetworkClient networkClient) {
        this.vmClassName = vmClassName;
        className = classNameConverter(vmClassName);
        this.classfileBuffer = classfileBuffer;
        this.classBeingRedefined = classBeingRedefined;
        this.loader = loader;
        this.networkClient=networkClient;

    }

    private static String classNameConverter(String vmClassName) {
        return vmClassName.replaceAll("/", ".");
    }

    public ClassLoader getLoader() {
        return loader;
    }

    public String getVmClassName() {
        return vmClassName;
    }

    public String getClassName() {
        return className;
    }

    public Class<?> getClassBeingRedefined() {
        return classBeingRedefined;
    }

    public byte[] getClassfileBuffer() {
        return classfileBuffer;
    }

    public PrimeMinisterNetworkClient getNetworkClient() {
        return networkClient;
    }




    @Override
    public String toString() {
        return "ClassDescInfoHolder{" +
                "loader=" + loader +
                ", vmClassName='" + vmClassName + '\'' +
                ", className='" + className + '\'' +
                ", classBeingRedefined=" + classBeingRedefined +
                '}';
    }
}
