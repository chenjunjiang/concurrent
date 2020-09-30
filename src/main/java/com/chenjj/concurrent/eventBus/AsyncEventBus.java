package com.chenjj.concurrent.eventBus;

import java.util.concurrent.ThreadPoolExecutor;

public class AsyncEventBus extends EventBus {
    AsyncEventBus(String busName, EventExceptionHandler eventExceptionHandler, ThreadPoolExecutor executor) {
        super(busName, eventExceptionHandler, executor);
    }

    public AsyncEventBus(String busName, ThreadPoolExecutor executor) {
        this(busName, null, executor);
    }

    public AsyncEventBus(ThreadPoolExecutor executor) {
        this("default-async", null, executor);
    }

    public AsyncEventBus(EventExceptionHandler eventExceptionHandler, ThreadPoolExecutor executor) {
        this("default-async", eventExceptionHandler, executor);
    }
}
