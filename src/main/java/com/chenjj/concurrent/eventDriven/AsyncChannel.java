package com.chenjj.concurrent.eventDriven;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 提供了对Message的并发处理能力
 */
public abstract class AsyncChannel implements Channel<Event> {
    private final ExecutorService executorService;

    public AsyncChannel() {
        this(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2));
    }

    protected AsyncChannel(ExecutorService executorService) {
        this.executorService = executorService;
    }

    /**
     * 处理Message
     *
     * @param message
     */
    @Override
    public final void dispatch(Event message) {
        this.executorService.submit(() -> {
            this.handle(message);
        });
    }

    /**
     * 具体处理逻辑由子类来实现
     *
     * @param message
     */
    protected abstract void handle(Event message);

    void stop() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}
