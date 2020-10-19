package com.chenjj.concurrent.atomic;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * volatile并不能保证原子性，下面的例子会导致金额有问题
 */
public class AtomicReferenceTest1 {
    static volatile DebitCard debitCard = new DebitCard("Alex", 0);

    public static void main(String[] args) {
        IntStream.range(1, 10).forEach(i -> {
            new Thread("T-" + i) {
                @Override
                public void run() {
                    while (true) {
                        final DebitCard dc = debitCard;
                        // 增加10元并产生一个新的DebitCard
                        DebitCard newDc = new DebitCard(dc.getAccount(), dc.getAmount() + 10);
                        System.out.println(newDc);
                        debitCard = newDc;
                        try {
                            TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(20));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        });
    }
}
