package com.chenjj.concurrent.activeObjects.standard;

import java.util.LinkedList;

/**
 * 用于存放调用线程通过Proxy提交过来的MethodMessage，允许存放无限的MethodMessage(理论上可以存放无限多个直到内存溢出)
 */
public class ActiveMessageQueue {
    // 存放MethodMessage
    private final LinkedList<MethodMessage> messages = new LinkedList<>();

    public ActiveMessageQueue() {
        // 启动线程
        new ActiveDaemonThread(this).start();
    }

    public void offer(MethodMessage methodMessage) {
        synchronized (this) {
            messages.addLast(methodMessage);
            // 因为只有一个线程负责take数据，所有使用notify即可
            this.notify();
        }
    }

    protected MethodMessage take() {
        synchronized (this) {
            while (messages.isEmpty()) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return messages.removeFirst();
        }
    }

}
