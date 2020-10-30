package com.chenjj.concurrent.utils;

import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * 为什么在调用register方法的时候会进入阻塞等待状态呢？其实原因很简单，我们都知道，
 * 当Phaser的某个Phase（阶段）的所有分片任务全都抵达时，会触发onAdvance方法的调用。
 * 如果在onAdvance方法执行的过程中有新的线程要求加入Phaser，比较合理的做法就是Phaser做好收尾工作之后再接纳新的分片任务进来，否则就会出现矛盾。
 * 比如，新的分区进来返回了当前的Phase（阶段）编号，但是当前阶段在进行结束收尾操作时却没有新的分区任务什么事，
 * 所以等待是一个比较合理的设计，但是有一点需要注意的是：如果有一个线程因为执行了Phaser的register方法而进入阻塞等待状态，
 * 尤其是该线程还无法被其他线程执行中断操作，那么尽可能不要在onAdvance方法中写入过多复杂且耗时的逻辑。
 */
public class PhaserTest5 {
    public static void main(String[] args) throws InterruptedException {
        final Phaser phaser = new Phaser(1) {
            @Override
            protected boolean onAdvance(int phase, int registeredParties) {
                try {
                    TimeUnit.MINUTES.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return super.onAdvance(phase, registeredParties);
            }
        };
        // 启动线程，然后执行arrive，保证当前阶段已完成，会使得onAdvance方法执行
        new Thread(phaser::arrive).start();
        // 确保线程先启动
        TimeUnit.SECONDS.sleep(2);
        long startTime = System.currentTimeMillis();
        // 此处调用register要阻塞，直到上一阶段的onAdvance执行完成
        int phaseNumber = phaser.register();
        assert phaseNumber == 1 : "current phase number is 1";
        System.out.println("register ELT: " + (System.currentTimeMillis() - startTime));
    }
}
