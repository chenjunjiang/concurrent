package com.chenjj.concurrent.eventBus;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class AsyncEventBusTest {
    public static void main(String[] args) {
        Bus bus = new AsyncEventBus("TestAsyncEventBus", (ThreadPoolExecutor) Executors.newFixedThreadPool(10));
        bus.register(new SimpleSubscriber1());
        bus.register(new SimpleSubscriber2());
        bus.post("Hello");
        System.out.println("------------------");
        bus.post("Hello", "test");
        // 由于这里使用的是线程池，所以必须关闭，否则线程不退出
        bus.close();
    }
}
