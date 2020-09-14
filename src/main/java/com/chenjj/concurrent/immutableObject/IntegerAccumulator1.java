package com.chenjj.concurrent.immutableObject;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * 设计为不可变的累加器对象
 */
public final class IntegerAccumulator1 {
    private final int init;

    public IntegerAccumulator1(int init) {
        this.init = init;
    }

    public IntegerAccumulator1(IntegerAccumulator1 accumulator, int init) {
        this.init = accumulator.getValue() + init;
    }

    /**
     * 每次相加都会产生一个新的IntegerAccumulator1
     *
     * @param i
     * @return
     */
    public IntegerAccumulator1 add(int i) {
        return new IntegerAccumulator1(this, i);
    }

    public int getValue() {
        return this.init;
    }

    public static void main(String[] args) {
        IntegerAccumulator1 accumulator = new IntegerAccumulator1(0);
        IntStream.range(0, 3).forEach(i -> new Thread(() -> {
            int inc = 0;
            while (true) {
                int oldValue = accumulator.getValue();
                int result = accumulator.add(inc).getValue();
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
