package com.chenjj.concurrent.activeObjects.general;

import com.chenjj.concurrent.activeObjects.standard.MethodMessage;

/**
 * 主要用于从ActiveMessageQueue中获取ActiveMessage然后执行
 */
public class ActiveDaemonThread extends Thread {
    private final ActiveMessageQueue queue;

    public ActiveDaemonThread(ActiveMessageQueue queue) {
        super("ActiveDaemonThread");
        this.queue = queue;
        // 设置为守护线程
        setDaemon(true);
    }

    @Override
    public void run() {
        for (; ; ) {
            ActiveMessage activeMessage = this.queue.take();
            activeMessage.execute();
        }
    }
}
