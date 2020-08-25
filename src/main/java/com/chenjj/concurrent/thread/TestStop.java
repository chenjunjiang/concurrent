package com.chenjj.concurrent.thread;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * stop()方法已经被废弃了，该方法在关闭线程时可能不会释放调monitor锁，所以强烈建议不要使用该方法结束线程。
 * 正常关闭：
 * 1、线程结束生命周期正常结束
 * 线程运行结束后，完成了自己的使命之后，就会正常退出，如果线程中的任务耗时比较短，或者时间可控，那么放任它正常结束就好了。
 * 2、捕获中断信号关闭线程
 * 见下面的示例：
 * 3、使用volatile开关控制
 * 由于线程的interrupt标识很有可能被擦除，或者逻辑单元中不会调用任何可中断方法，所以使用volatile修饰的开关flag关闭线程
 * 也是一种常用的方法。
 */
public class TestStop {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread() {
            /*@Override
            public void run() {
                System.out.println("I will start work");
                // 通过检查线程interrupt的标识来决定是否退出
                while (!isInterrupted()) {
                    // working
                }
                System.out.println("I will be exiting.");
            }*/

            @Override
            public void run() {
                System.out.println("I will start work");
                for (; ; ) {
                    // working
                    // 如果在线程中执行某个可中断方法，则可以通过捕获中断信号来决定是否退出
                    try {
                        TimeUnit.MINUTES.sleep(1);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        };
        /*thread.start();
        TimeUnit.MINUTES.sleep(1);
        System.out.println("System will be shutdown.");
        thread.interrupt();*/

        MyTask myTask = new MyTask();
        myTask.start();
        TimeUnit.MINUTES.sleep(1);
        System.out.println("System will be shutdown.");
        myTask.close();
    }

    static class MyTask extends Thread {
        private volatile boolean closed = false;

        @Override
        public void run() {
            System.out.println("I will start work");
            while (!closed && !isInterrupted()) {
                // 正在运行
            }
            System.out.println("I will be exiting.");
        }

        public void close() {
            this.closed = true;
            this.interrupt();
        }
    }
}
