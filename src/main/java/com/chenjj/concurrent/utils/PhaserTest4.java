package com.chenjj.concurrent.utils;

import java.util.concurrent.Phaser;

public class PhaserTest4 {
    public static void main(String[] args) {
        final Phaser phaser = new Phaser(2) {
            /**
             * @param phase
             * @param registeredParties
             * @return true if this phaser should terminate，如果要终止phaser，那么就返回true
             */
            @Override
            protected boolean onAdvance(int phase, int registeredParties) {
                return phase >= 1;
            }
        };
        // 调用两次arrive方法
        phaser.arrive();
        phaser.arrive();
        assert phaser.getPhase() == 1 : "so far, the phase number is 1.";
        /**
         * Phaser首次编号为0，第一阶段完成时会调用onAdvance方法，此时的phase参数为0，
         * 在执行完onAdvance方法后，会产生新的编号，这时编号变成1；
         * 下面再次完成一个阶段后，onAdvance方法的phase参数为1，此时返回值为true，phaser将被终止。
         */
        // 调用两次arrive方法
        phaser.arrive();
        phaser.arrive();
        // phaser终止之后该方法将返回负数
        assert phaser.getPhase() < 0 : "so far, the phase number is negative value.";
        assert phaser.isTerminated() : "phaser is terminated now.";
        // 在Phaser被终止之后，调用相关的方法不会出现异常，但是也并不会工作。所以这里调用arriveAndAwaitAdvance()方法并不会等待其他分区任务到达，而是直接返回
        phaser.arriveAndAwaitAdvance();
    }
}
