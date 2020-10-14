package com.chenjj.concurrent.jmh.profiler;

public class Alex {
    static {
        System.out.println("Alex2 Class is Initialized.");
    }

    private String name = "chenjunjiang";
    private int age = 32;
    private byte[] data = new byte[1024 * 10];
}
