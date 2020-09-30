package com.chenjj.concurrent.eventBus;

/**
 * 同步的EventBus有个缺点，若其中的一个subscribe方法运行时间比较长，则会影响下一个subscribe方法的执行
 */
public class EventBusTest {
    public static void main(String[] args) {
        Bus bus = new EventBus("TestEventBus");
        bus.register(new SimpleSubscriber1());
        bus.register(new SimpleSubscriber2());
        bus.post("Hello");
        System.out.println("------------------");
        bus.post("Hello", "test");
    }
}
