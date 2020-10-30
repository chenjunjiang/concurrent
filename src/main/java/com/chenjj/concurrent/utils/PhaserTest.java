package com.chenjj.concurrent.utils;

import java.util.Date;
import java.util.concurrent.Phaser;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * 将Phaser当作CountDownLatch来使用
 */
public class PhaserTest {
    public static void main(String[] args) throws InterruptedException {
        // 未指定parties，此时为默认为0，后面可以通过register方法增加
        final Phaser phaser = new Phaser();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                // 让Phaser的parties加一
                phaser.register();
                try {
                    TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(20));
                    phaser.arrive();
                    System.out.println(new Date() + ":" + Thread.currentThread() + " completed the work.");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, "T-" + i).start();
        }

        // 这里休眠的目的是等phaser的parties变为10
        TimeUnit.SECONDS.sleep(10);
        // 主线程也调用register，此时的parties为11
        phaser.register();
        // 主线程也arrive，但是它要等待下一个阶段，等到下一个阶段的前提是所有线程都arrive，也就是phaser内部当前phase的unarrived数量为0
        phaser.arriveAndAwaitAdvance();
        assert phaser.getRegisteredParties() == 11 : "total 11 parties is registered.";
        System.out.println(new Date() + ":all of sub task completed the work.");
    }
}
