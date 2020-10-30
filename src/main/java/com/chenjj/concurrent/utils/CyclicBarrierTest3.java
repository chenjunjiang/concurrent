package com.chenjj.concurrent.utils;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierTest3 {
    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
        final CyclicBarrier barrier = new CyclicBarrier(1);
        barrier.await();
        barrier.await();
        barrier.await();
    }
}
