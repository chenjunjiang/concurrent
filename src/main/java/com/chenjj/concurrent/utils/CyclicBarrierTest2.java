package com.chenjj.concurrent.utils;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class CyclicBarrierTest2 {
    /**
     * 在主线程中的两次await中间为何没有对barrier进行reset的操作，
     * 那是因为在CyclicBarrier内部维护了一个count。当所有的await调用导致其值为0的时候，
     * reset相关的操作会被默认执行。
     *
     * @param args
     * @throws BrokenBarrierException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
        final CyclicBarrier barrier = new CyclicBarrier(11);
        IntStream.rangeClosed(1, 10).forEach((i) -> new Thread(new Tourist(i, barrier)).start());
        // 主线程进入阻塞，等待所有的游客都上车
        barrier.await();
        System.out.println("Tour Guider:all of Tourist get on the bus.");
        // 主线程进入阻塞，等待所有的游客都下车
        barrier.await();
        System.out.println("Tour Guider:all of Tourist get off the bus.");
    }

    private static class Tourist implements Runnable {
        private final int touristID;
        private final CyclicBarrier barrier;

        private Tourist(int touristID, CyclicBarrier barrier) {
            this.touristID = touristID;
            this.barrier = barrier;
        }

        @Override
        public void run() {
            System.out.printf("Tourist:%d by bus\n", touristID);
            // 模拟乘客上车的时间开销
            this.spendSeveralSeconds();
            // 上车后等待其它同伴上车
            this.awaitAndPrint("Tourist:%d Get on the bus, and wait other people reached.\n");
            System.out.printf("Tourist:%d arrive the destination\n", touristID);
            // 模拟乘客下车的时间开销
            this.spendSeveralSeconds();
            // 下车后，等待其他同伴全部下车
            this.awaitAndPrint("Tourist:%d Get off the bus, and wait other people get off.\n");
        }

        private void awaitAndPrint(String message) {
            System.out.printf(message, touristID);
            try {
                int result = barrier.await();
                System.out.println("result=================" + result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }

        private void spendSeveralSeconds() {
            try {
                TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(10));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
