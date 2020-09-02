package com.chenjj.concurrent.classloader;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class ClassInit {
    public ClassInit() {
        System.out.println("ClassInit constructor:" + Thread.currentThread().getName());
    }

    /**
     * 静态代码块只会在初始化的时候被执行一次
     */
    static {
        System.out.println("The ClassInit static code block will be invoke." + Thread.currentThread().getName());
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        IntStream.range(0, 5).forEach(i -> {
            new Thread(ClassInit::new).start();
        });
    }
}
