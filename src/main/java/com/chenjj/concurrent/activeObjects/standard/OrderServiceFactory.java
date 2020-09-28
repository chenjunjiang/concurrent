package com.chenjj.concurrent.activeObjects.standard;

/**
 * 为了让OrderServiceProxy的构造透明化，通过Factory来构建OrderService
 */
public class OrderServiceFactory {
    // 保证整个JVM进程中唯一
    private final static ActiveMessageQueue activeMessageQueue = new ActiveMessageQueue();

    private OrderServiceFactory() {

    }

    public static OrderService toActiveObject(OrderService orderService) {
        return new OrderServiceProxy(orderService, activeMessageQueue);
    }
}
