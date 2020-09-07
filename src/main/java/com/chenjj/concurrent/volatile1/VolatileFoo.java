package com.chenjj.concurrent.volatile1;

import java.util.concurrent.TimeUnit;

public class VolatileFoo {
    private final static int MAX = 5;
    private static volatile int initValue = 0;

    public static void main(String[] args) {
        // 启动Reader线程
        new Thread(() -> {
            int localValue = initValue;
            while (localValue < MAX) {
                // 当initValue和localValue不相等的时候输出initValue被修改的信息
                if (initValue != localValue) {
                    System.out.printf("The initValue is updated to [%d]\n", initValue);
                    localValue = initValue;
                }
            }
        }, "Reader").start();

        // 启动Updater线程,对initValue进行修改
        new Thread(() -> {
            int localValue = initValue;
            while (localValue < MAX) {
                System.out.printf("The initValue will be changed to [%d]\n", ++localValue);
                initValue = localValue;
                try {
                    // 短暂休眠，目的是为了Reader线程能够及时输出变化内容
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Updater").start();
    }
}
