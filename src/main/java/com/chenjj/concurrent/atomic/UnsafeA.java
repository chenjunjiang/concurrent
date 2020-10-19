package com.chenjj.concurrent.atomic;

public class UnsafeA {
    private int i = 0;

    public UnsafeA() {
        this.i = 10;
    }

    public int getI() {
        return i;
    }
}
