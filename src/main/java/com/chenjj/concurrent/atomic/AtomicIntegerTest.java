package com.chenjj.concurrent.atomic;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 要使用assert需要加-ea（enable assertion） jvm参数
 */
public class AtomicIntegerTest {
    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(10);
        assert atomicInteger.getAndUpdate(x -> x + 2) == 10;
        assert atomicInteger.get() == 12;
    }
}
