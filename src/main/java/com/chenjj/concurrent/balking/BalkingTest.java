package com.chenjj.concurrent.balking;

public class BalkingTest {
    public static void main(String[] args) {
        new DocumentEditThread("D:\\", "balking.txt").start();
    }
}
