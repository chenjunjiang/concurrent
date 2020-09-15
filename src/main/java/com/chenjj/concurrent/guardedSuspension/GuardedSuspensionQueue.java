package com.chenjj.concurrent.guardedSuspension;

import java.util.LinkedList;

/**
 * Guarded Suspension设计模式
 * 是确保挂起的意思。当线程在访问某个对象时，发现临界值的条件不满足时，就暂时挂起等待条件满足时再次访问。
 */
public class GuardedSuspensionQueue {
    // 定义queue
    private final LinkedList<Integer> queue = new LinkedList<>();
    // 定义queue的最大数量
    private final int LIMIT = 100;

    /**
     * 向queue插入数据，如果queue中的元素数量超过了最大数量，当前线程则会挂起
     */
    public void offer(Integer data) throws InterruptedException {
        synchronized (this) {
            while (queue.size() >= LIMIT) {
                this.wait();
            }
            queue.add(data);
            this.notifyAll();
        }
    }

    public Integer take() throws InterruptedException {
        synchronized (this) {
            while (queue.isEmpty()) {
                this.wait();
            }
            Integer data = queue.removeFirst();
            this.notifyAll();
            return data;
        }
    }
}
