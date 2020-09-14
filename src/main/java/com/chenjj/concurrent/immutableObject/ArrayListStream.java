package com.chenjj.concurrent.immutableObject;

import java.util.Arrays;
import java.util.List;

/**
 * 有些非线程安全的可变对象被不可变机制加以处理之后，照样可以具备不可变性，比如ArrayList生成的stream
 * 在多线程情况下也是线程安全的。
 */
public class ArrayListStream {
    public static void main(String[] args) {
        /**
         * list虽然是在并行的环境下运行的，但是在stream中的每一个操作都是生成一个全新的List，根本不会影响原始的list
         */
        List<String> list = Arrays.asList("Java", "Thread", "Concurrency", "Scala", "Clojure");
        list.parallelStream().map(String::toUpperCase).forEach(System.out::println);
        System.out.println(list);
    }
}
