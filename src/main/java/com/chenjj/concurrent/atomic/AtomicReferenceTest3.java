package com.chenjj.concurrent.atomic;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

/**
 * AtomicReferenceTest2似乎满足了我们的需求，但是它却是一种阻塞式的解决方案，同一时刻只能有一个线程真正在工作，其他线程都将陷入阻塞，
 * 因此这并不是一种效率很高的解决方案，这个时候就可以利用AtomicReference的非阻塞原子性解决方案提供更加高效的方式了。
 * <p>
 * DebitCard{account='Alex', amount=30}
 * DebitCard{account='Alex', amount=10}
 * DebitCard{account='Alex', amount=20}
 * DebitCard{account='Alex', amount=40}
 * DebitCard{account='Alex', amount=50}
 * DebitCard{account='Alex', amount=60}
 * DebitCard{account='Alex', amount=70}
 * DebitCard{account='Alex', amount=80}
 * 由于非阻塞的缘故，数值20的输出有可能会出现在数值10的前面，数值130的输出则出现在了数值110的前面，
 * 但这并不妨碍amount的数值是按照10的步长增长的。
 */
public class AtomicReferenceTest3 {
    private static AtomicReference<DebitCard> debitCardAtomicReference = new AtomicReference<>(new DebitCard("Alex", 0));

    public static void main(String[] args) {
        IntStream.range(1, 10).forEach(i -> {
            new Thread("T-" + i) {
                @Override
                public void run() {
                    while (true) {
                        final DebitCard dc = debitCardAtomicReference.get();
                        DebitCard newDc = new DebitCard(dc.getAccount(), dc.getAmount() + 10);
                        if (debitCardAtomicReference.compareAndSet(dc, newDc)) {
                            System.out.println(newDc);
                        }
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
