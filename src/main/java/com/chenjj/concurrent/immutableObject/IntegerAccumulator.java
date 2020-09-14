package com.chenjj.concurrent.immutableObject;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class IntegerAccumulator {
    private int init;

    public IntegerAccumulator(int init) {
        this.init = init;
    }

    public int add(int i) {
        this.init += i;
        return this.init;
    }

    public int getValue() {
        return this.init;
    }

    public static void main(String[] args) {
        // 对共享资源没有进行任何同步，会出现问题
        /*IntegerAccumulator accumulator = new IntegerAccumulator(0);
        IntStream.range(0, 3).forEach(i -> new Thread(() -> {
            int inc = 0;
            while (true) {
                int oldValue = accumulator.getValue();
                int result = accumulator.add(inc);
                System.out.println(oldValue + "+" + inc + "=" + result);
                if (inc + oldValue != result) {
                    System.out.println("ERROR: " + oldValue + "+" + inc + "=" + result);
                }
                inc++;
                slowly();
            }
        }).start());*/

        // 使用synchronized进行同步
        IntegerAccumulator accumulator = new IntegerAccumulator(0);
        IntStream.range(0, 3).forEach(i -> new Thread(() -> {
            int inc = 0;
            while (true) {
                int oldValue;
                int result;
                synchronized (IntegerAccumulator.class) {
                    oldValue = accumulator.getValue();
                    result = accumulator.add(inc);
                }
                System.out.println(oldValue + "+" + inc + "=" + result);
                if (inc + oldValue != result) {
                    System.out.println("ERROR: " + oldValue + "+" + inc + "=" + result);
                }
                inc++;
                slowly();
            }
        }).start());
    }

    private static void slowly() {
        try {
            TimeUnit.MILLISECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
