package com.chenjj.concurrent.threadPool;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.LongStream;

/**
 * 该示例通过高并发多线程的方式计算一个数组中所有元素之和，数组会被拆分成若干分片，每一个异步任务都会计算
 * 对应分片元素之和，最后所有的子任务结果会被join在一起作为最终的结果返回。
 */
public class RecursiveTaskTest extends RecursiveTask<Long> {
    private final long[] numbers;
    private final int startIndex;
    private final int endIndex;
    private static final long THRESHOLD = 10_000L;

    public static void main(String[] args) {
        long[] numbers = LongStream.rangeClosed(1, 9_000_000).toArray();
        RecursiveTaskTest recursiveTaskTest = new RecursiveTaskTest(numbers);

        // 最大并行任务数量为Runtime.getRuntime().availableProcessors()， 可参考makeCommonPool方法中的源码
        long sum = ForkJoinPool.commonPool().invoke(recursiveTaskTest);
        System.out.println(sum);

        assert sum == LongStream.rangeClosed(1, 9_000_000).sum();
    }

    public RecursiveTaskTest(long[] numbers) {
        this(numbers, 0, numbers.length);
    }

    public RecursiveTaskTest(long[] numbers, int startIndex, int endIndex) {
        this.numbers = numbers;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    @Override
    protected Long compute() {
        int length = endIndex - startIndex;
        // 当元素数量少于等于THRESHOLD时，任务将不必拆分
        if (length <= THRESHOLD) {
            long result = 0L;
            for (int i = startIndex; i < endIndex; i++) {
                result += numbers[i];
            }
            return result;
        }

        // 拆分任务(一分为二，被拆分后的任务可能还会被拆分：递归)
        int tempEndIndex = (startIndex + endIndex) / 2;
        // 第一个子任务
        RecursiveTaskTest firstTask = new RecursiveTaskTest(numbers, startIndex, tempEndIndex);
        // 异步执行第一个子任务(子任务有可能还会被拆分，将取决于元素数量)
        firstTask.fork();
        // 第二个子任务
        RecursiveTaskTest secondTask = new RecursiveTaskTest(numbers, tempEndIndex, endIndex);
        // 异步执行第二个子任务(子任务有可能还会被拆分，将取决于元素数量)
        secondTask.fork();

        // join等待子任务的运算结果
        long secondTaskResult = secondTask.join();
        long firstTaskResult = firstTask.join();

        return secondTaskResult + firstTaskResult;
    }
}
