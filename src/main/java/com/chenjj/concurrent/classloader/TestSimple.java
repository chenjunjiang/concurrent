package com.chenjj.concurrent.classloader;

public class TestSimple {
    public static void main(String[] args) {
        Simple[] simples = new Simple[10];
        System.out.println(simples.length);
        System.out.println(GlobalConstants.MAX);
        System.out.println(GlobalConstants.RANDOM);
    }
}
