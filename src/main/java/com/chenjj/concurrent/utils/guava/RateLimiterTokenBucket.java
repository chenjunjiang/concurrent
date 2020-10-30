package com.chenjj.concurrent.utils.guava;

import com.google.common.util.concurrent.Monitor;
import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class RateLimiterTokenBucket {
    // 当前活动商品数量
    private final static int MAX = 1000;
    // 订单编号，订单成功之后会产生一个新的订单
    private int orderID;
    // 1s内只允许10个用户能够成功抢到商品，也就是说订单服务将会被匀速地调用
    private final RateLimiter rateLimiter = RateLimiter.create(10.0D);
    private Monitor bookOrderMonitor = new Monitor();

    static class NoProductionException extends Exception {
        public NoProductionException(String message) {
            super(message);
        }
    }

    static class OrderFailedException extends Exception {
        public OrderFailedException(String message) {
            super(message);
        }
    }

    /**
     * 用户下单，只允许匀速地调用订单服务
     */
    public void bookOrder(Consumer<Integer> consumer) throws OrderFailedException, NoProductionException {
        // 如果当前商品有库存则进行抢购操作
        if (bookOrderMonitor.enterIf(bookOrderMonitor.newGuard(() -> orderID < MAX))) {
            try {
                // 抢购商品，最多等待100毫秒
                if (!rateLimiter.tryAcquire(100, TimeUnit.MILLISECONDS)) {
                    // 如果100毫秒之内抢购失败，则抛出异常，客户端可以再次尝试
                    throw new OrderFailedException("book order failed, please try again.");
                }
                /**
                 * 调用订单服务，进行订单逻辑处理
                 * 由于bookOrderMonitor.enterIf是加锁了的，为了提高吞吐量，最好是把订单逻辑处理
                 * 做成异步的
                 */
                orderID++;
                consumer.accept(orderID);
            } finally {
                bookOrderMonitor.leave();
            }
        } else {
            throw new NoProductionException("No available production now.");
        }
    }
}
