package com.chenjj.concurrent.threadPool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 将数组中的每一个元素并行增加10倍（每一个数字元素都将乘10）
 */
public class RecursiveActionTest extends RecursiveAction {
    private List<Integer> numbers;
    private static final int THRESHOLD = 10;
    private int start;
    private int end;
    private int factor;

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(ThreadLocalRandom.current().nextInt(1_000));
        }
        System.out.println(list);
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        RecursiveActionTest recursiveActionTest = new RecursiveActionTest(list, 0, list.size(), 10);
        forkJoinPool.invoke(recursiveActionTest);
        System.out.println(list);
    }

    public RecursiveActionTest(List<Integer> numbers, int start, int end, int factor) {
        this.numbers = numbers;
        this.start = start;
        this.end = end;
        this.factor = factor;
    }

    @Override
    protected void compute() {
        if (end - start < THRESHOLD) {
            // 直接计算
            computeDirectly();
        } else {
            // 拆分
            int middle = (end + start) / 2;
            RecursiveActionTest taskOne = new RecursiveActionTest(numbers, start, middle, factor);
            RecursiveActionTest taskTwo = new RecursiveActionTest(numbers, middle, end, factor);
            this.invokeAll(taskOne, taskTwo);
        }
    }

    private void computeDirectly() {
        for (int i = start; i < end; i++) {
            numbers.set(i, numbers.get(i) * factor);
        }
    }
}
