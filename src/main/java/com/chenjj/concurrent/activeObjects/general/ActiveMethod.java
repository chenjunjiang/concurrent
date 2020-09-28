package com.chenjj.concurrent.activeObjects.general;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 有该注解的方法可以转换为Active-Method，没有的就按照普通方法执行，不会被单独的线程执行
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ActiveMethod {
}
