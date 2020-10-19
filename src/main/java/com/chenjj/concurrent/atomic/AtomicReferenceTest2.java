package com.chenjj.concurrent.atomic;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * 多线程下加锁(synchronized)增加账号金额，这样能保证原子性，但是是阻塞方案
 */
public class AtomicReferenceTest2 {
    static volatile DebitCard debitCard = new DebitCard("Alex", 0);

    public static void main(String[] args) {
        IntStream.range(1, 10).forEach(i -> {
            new Thread("T-" + i) {
                @Override
                public void run() {
                    while (true) {
                        synchronized (AtomicReferenceTest2.class) {
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
                }
            }.start();
        });
    }
}
