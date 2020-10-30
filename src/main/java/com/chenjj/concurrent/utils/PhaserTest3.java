package com.chenjj.concurrent.utils;

import java.util.Date;
import java.util.concurrent.Phaser;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * 在构造CyclicBarrier的时候，如果给定一个Runnable作为回调，那么待所有的任务线程都到达barrier point之后，
 * 该Runnable接口的run方法将会被调用。同样，我们可以通过重写Phaser的onAdvance方法来实现类似的功能。
 * 在Phaser中，onAdvance方法是非常重要的，它在每一个Phase（阶段）中除了会在所有的分片都到达之后执行一次调用之外，
 * 更重要的是，它还会决定该Phaser是否被终止（当onAdvance方法的返回值为true时，则表明该Phaser将被终止，
 * 接下来将不能再使用）。下面的例子会让Phaser也支持CyclicBarrier式的回调操作。
 */
public class PhaserTest3 {
    public static void main(String[] args) {
        final Phaser phaser = new MyPhaser(() -> {
            System.out.println(new Date() + ":all of sub task completed work.");
        });
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                phaser.register();
                try {
                    TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(20));
                    // 等待所有线程arrive，然后继续执行
                    phaser.arriveAndAwaitAdvance();
                    System.out.println(new Date() + ":" + Thread.currentThread() + " completed the work.");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, "T-" + i).start();
        }
    }

    private static class MyPhaser extends Phaser {
        private final Runnable runnable;

        private MyPhaser(Runnable runnable) {
            super();
            this.runnable = runnable;
        }

        /**
         * 当parties个任务都到达某个phase时该方法将会调用
         *
         * @param phase
         * @param registeredParties
         * @return
         */
        @Override
        protected boolean onAdvance(int phase, int registeredParties) {
            this.runnable.run();
            return super.onAdvance(phase, registeredParties);
        }
    }
}
