package com.chenjj.concurrent.threadPool;

@FunctionalInterface
public interface ThreadFactory {
    Thread createThread(Runnable runnable);
}
