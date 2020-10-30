package com.chenjj.concurrent.utils.guava;

import com.google.common.util.concurrent.Monitor;

public class MonitorTest {
    // 定义Monitor
    private static Monitor monitor = new Monitor();
    // 共享数据
    private static int x = 0;
    // 临界值，共享数据的值不能超过MAX_VALUE;
    private static final int MAX_VALUE = 10;
    // 定义Guard并且实现isSatisfied方法
    private static final Monitor.Guard INC_WHNE_LESS_10 = new Monitor.Guard(monitor) {
        /**
         * 该方法就相当于我们在写对象监视器或者Condition时的临界值判断
         * @return
         */
        @Override
        public boolean isSatisfied() {
            return x < MAX_VALUE;
        }
    };

    /**
     * 当某个线程进入Monitor代码块时，实际上它首先要抢占与Monitor关联的Lock，当该线程调用了leave方法，
     * 实际上是需要释放与Monitor关联的Lock，因此在某个时刻仅有一个线程能够进入到Monitor代码块中（排他的）。
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        while (true) {
            /**
             * enterWhen除了具备锁的功能之外还具备临界值判断的操作，因此只有当x满足临界值判断时当前线程才
             * 会对x进行自增运算，否则当前线程将会进入阻塞队列（其实在Guard内部使用的也是Condition）。
             */
            monitor.enterWhen(INC_WHNE_LESS_10);
            try {
                x++;
                System.out.println(Thread.currentThread() + ": x value is:" + x);
            } finally {
                /**
                 * 该方法除了释放当前的锁之外，还会通知唤醒与Guard关联的Condition阻塞队列中的某个阻塞线程。
                 */
                monitor.leave();
            }
        }
    }
}
