package com.chenjj.concurrent.activeObjects.standard;

import com.chenjj.concurrent.future.Future;

import java.util.HashMap;
import java.util.Map;

/**
 * 将OrderService的每一个方法都封装成MethodMessage，然后提交给ActiveMessage队列，在使用OrderService接口方法的时候，
 * 实际上是调用OrderServiceProxy中的方法。
 */
public class OrderServiceProxy implements OrderService {
    private final OrderService orderService;
    private final ActiveMessageQueue activeMessageQueue;

    public OrderServiceProxy(OrderService orderService, ActiveMessageQueue activeMessageQueue) {
        this.orderService = orderService;
        this.activeMessageQueue = activeMessageQueue;
    }

    @Override
    public Future<String> findOrderDetails(long orderId) {
        final ActiveFuture<String> activeFuture = new ActiveFuture<>();
        Map<String, Object> params = new HashMap<>();
        params.put("orderId", orderId);
        params.put("activeFuture", activeFuture);
        MethodMessage methodMessage = new FindOrderDetailsMessage(params, orderService);
        activeMessageQueue.offer(methodMessage);
        return activeFuture;
    }

    @Override
    public void order(String account, long orderId) {
        Map<String, Object> params = new HashMap<>();
        params.put("account", account);
        params.put("orderId", orderId);
        MethodMessage methodMessage = new OrderMessage(params, orderService);
        activeMessageQueue.offer(methodMessage);
    }
}
