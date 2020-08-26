package com.chenjj.concurrent.threadCommunication;

import java.util.concurrent.TimeUnit;

/**
 * 假设提交没有任何延迟，而处理event可能要花费比提交更多的时间
 */
public class EventClient {
    public static void main(String[] args) {
        final EventQueue eventQueue = new EventQueue();
        new Thread(() -> {
            for (; ; ) {
                eventQueue.offer(new EventQueue.Event());
            }
        }, "Producer").start();

        new Thread(() -> {
            for (; ; ) {
                eventQueue.take();
                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Consumer").start();
    }
}
