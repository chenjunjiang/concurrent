package com.chenjj.concurrent.classloader;

public class TestFloat {
    public static void main(String[] args) {
        Float f = new Float(1.1f);
        // null，由根类加载器加载
        System.out.println(f.getClass().getClassLoader());
    }
}
