package com.chenjj.concurrent.workThread;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class Test {
    public static void main(String[] args) {
        // 流水线上有5个工人
        final ProductionChannel channel = new ProductionChannel(5);
        final Random random = new Random(System.currentTimeMillis());
        AtomicInteger productionNo = new AtomicInteger();
        // 8个工作人员不断地往传送带上放半成品
        IntStream.range(1, 8).forEach(i -> new Thread(() -> {
            while (true) {
                channel.offerProduction(new Production(productionNo.getAndIncrement()));
                try {
                    TimeUnit.SECONDS.sleep(random.nextInt(10));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start());
    }
}
