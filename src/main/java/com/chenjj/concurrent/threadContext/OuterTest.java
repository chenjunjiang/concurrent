package com.chenjj.concurrent.threadContext;

public class OuterTest {
    public static void main(String[] args) {
        Outer outer = new Outer();
        outer.setId(1);
        outer = null;
        System.gc();
        // 静态内部类的实例不依赖外部类实例
        Outer.Inner inner = new Outer.Inner();
        inner.setName("chenjj");
        // 普通内部类的实例依赖外部类实例
        // Outer.Inner1 inner1 = outer.new Inner1();
        System.out.println(inner.getName());
    }
}
