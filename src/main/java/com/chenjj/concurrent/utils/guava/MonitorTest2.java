package com.chenjj.concurrent.utils.guava;

import com.google.common.util.concurrent.Monitor;

import java.util.concurrent.TimeUnit;

public class MonitorTest2 {
    // 定义Monitor
    private static Monitor monitor = new Monitor();
    // 共享数据
    private static int x = 0;
    // 临界值，共享数据的值不能超过MAX_VALUE;
    private static final int MAX_VALUE = 10;

    /**
     * 当某个线程进入Monitor代码块时，实际上它首先要抢占与Monitor关联的Lock，当该线程调用了leave方法，
     * 实际上是需要释放与Monitor关联的Lock，因此在某个时刻仅有一个线程能够进入到Monitor代码块中（排他的）。
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        new Thread(MonitorTest2::test).start();
        TimeUnit.SECONDS.sleep(1);
        test();
    }

    public static void test() {
        if (monitor.enterIf(monitor.newGuard(() -> x < MAX_VALUE))) {
            try {
                x++;
                System.out.println(Thread.currentThread() + ": x value is:" + x);
                TimeUnit.SECONDS.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                /**
                 * 该方法除了释放当前的锁之外，还会通知唤醒与Guard关联的Condition阻塞队列中的某个阻塞线程。
                 */
                monitor.leave();
            }
        }
    }
}
