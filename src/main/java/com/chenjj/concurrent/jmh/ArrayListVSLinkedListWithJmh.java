package com.chenjj.concurrent.jmh;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 当我使用jmh的2.26版本(当时是最新)运行时，arrayListAdd方法会出现内存溢出的情况
 */
@BenchmarkMode({Mode.AverageTime})
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Thread)
public class ArrayListVSLinkedListWithJmh {
    private final static String DATA = "DUMMY DATA";
    private List<String> arrayList;
    private List<String> linkedList;

    public static void main(String[] args) throws RunnerException {
        final Options options = new OptionsBuilder().include(ArrayListVSLinkedListWithJmh.class
                .getSimpleName())
                .forks(1)
                .measurementIterations(10) // 度量执行的批次为10，在这10个批次中，对基准方法的执行与调用将会纳入统计
                .warmupIterations(10)// 在真正度量前，首先会对代码进行10个批次的热身，使代码在度量运行的时候达到JVM已经优化后的效果
                .build();
        new Runner(options).run();
    }

    @Setup(Level.Iteration)
    public void setUp() {
        this.arrayList = new ArrayList<>();
        this.linkedList = new LinkedList<>();
    }

    /**
     * 与Junit4.x版本需要使用@Test注解标记单元测试方法一样，JMH对基准测试的方法需要使用@Benchmark注解进行标记，
     * 否则方法将被视为普通方法，并且不会对其执行基准测试。如果一个类中没有任何基准测试方法（被@Benchmark标记的方法），
     * 那么对其进行基准测试则会出现异常。
     *
     * @return
     */
    @Benchmark
    public List<String> arrayListAdd() {
        // System.out.println("开始插入数据......");
        this.arrayList.add(DATA);
        return arrayList;
    }

    @Benchmark
    public List<String> linkedListAdd() {
        this.linkedList.add(DATA);
        return linkedList;
    }
}
