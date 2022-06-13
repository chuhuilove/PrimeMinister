package com.chuhui.primeminister.collector.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * PrimeMinisterTrace
 *
 * @author: cyzi
 * @Date: 6/6/22
 * @Description:
 */
@Target(value = {ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PrimeMinisterTrace {



}
