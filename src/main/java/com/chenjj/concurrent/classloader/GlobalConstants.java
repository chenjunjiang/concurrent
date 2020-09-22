package com.chenjj.concurrent.classloader;

import java.util.Random;

public class GlobalConstants {
    static {
        System.out.println("The GlobalConstants will be initialized.");
    }

    // 其它类中使用MAX不会导致GlobalConstants的加载和初始化
    public final static int MAX = 100;
    /**
     * 虽然RANDOM是静态常量，但是由于需要进行随机函数计算，在类的加载、连接阶段是无法对其进行计算的，
     * 需要初始化之后才能对其赋予准确的值，因此其它类中使用RANDOM会导致GlobalConstants初始化，
     * 当然也会导致GlobalConstants被加载，因为加载过程在初始化之前
     */
    public final static int RANDOM = new Random().nextInt();
}
