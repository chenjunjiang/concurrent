package com.chenjj.concurrent.jmh.rightCoding;

import com.chenjj.concurrent.jmh.param.JMHParam2;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@Fork(1)
@Warmup(iterations = 5)
@Measurement(iterations = 10)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Group)
public class JMHMap {
    // 为type提供四种可配置的参数值
    @Param({"1", "2", "3", "4"})
    private int type;
    private Map<Integer, Integer> map;

    public static void main(String[] args) throws RunnerException {
        final Options options = new OptionsBuilder().include(JMHMap.class.getSimpleName()).build();
        new Runner(options).run();
    }

    @Setup
    public void setUp() {
        switch (type) {
            case 1:
                this.map = new ConcurrentHashMap<>();
                break;
            case 2:
                this.map = new ConcurrentSkipListMap<>();
                break;
            case 3:
                this.map = new Hashtable<>();
                break;
            case 4:
                this.map = Collections.synchronizedMap(new HashMap<>());
                break;
            default:
                throw new IllegalArgumentException("Illegal map type.");
        }
    }

    @Group("g")
    @GroupThreads(5)
    @Benchmark
    public void putMap() {
        int random = randomIntValue();
        this.map.put(random, random);
    }

    @Group("g")
    @GroupThreads(5)
    @Benchmark
    public void getMap() {
        this.map.get(randomIntValue());
    }

    private int randomIntValue() {
        return (int) Math.ceil(Math.random() * 600000);
    }
}
