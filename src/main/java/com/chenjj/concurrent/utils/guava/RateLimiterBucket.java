package com.chenjj.concurrent.utils.guava;

import com.google.common.util.concurrent.Monitor;
import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

public class RateLimiterBucket {
    // 漏桶采用线程安全的容器
    private final ConcurrentLinkedQueue<Request> bucket = new ConcurrentLinkedQueue<>();
    // 容量
    private final static int BUCKET_CAPACITY = 1000;
    // 定义漏桶的下沿流水速率，每秒均匀放行10个request
    private final RateLimiter rateLimiter = RateLimiter.create(10.0d);
    // 提交请求时需要用到的Monitor
    private final Monitor requestMonitor = new Monitor();
    // 处理请求时需要用到的Monitor
    private final Monitor handleMonitor = new Monitor();

    public void submitRequest(int data) {
        this.submitRequest(new Request(data));
    }

    /**
     * 接收来自客户端提交的请求数据
     *
     * @param request
     */
    private void submitRequest(Request request) {
        // 当漏桶容量未溢出时
        if (requestMonitor.enterIf(requestMonitor.newGuard(() -> bucket.size() < BUCKET_CAPACITY))) {
            try {
                // 在桶中加入新的request
                boolean result = bucket.offer(request);
                if (result) {
                    System.out.println(Thread.currentThread() + " submit request:" + request.getData() + " successfully.");
                } else {
                    // produce into MQ and will try again later.
                }
            } finally {
                requestMonitor.leave();
            }
        } else {
            System.out.println("The request:" + request.getData() + " will be down-dimensional handle due to bucket is overflow.");
            // produce into MQ and will try again later.
        }
    }

    /**
     * 从漏桶中匀速地处理相关请求
     *
     * @param consumer
     */
    public void handleRequest(Consumer<Request> consumer) {
        /**
         * 若漏桶中存在请求，则处理
         * 注意：这个地方线程满足条件后在获取锁的时候可能会阻塞，如果下面数据处理过程时间较长，
         * 导致多个线程阻塞于此，会影响吞吐量；所以数据处理过程最好做成异步的
         */
        if (handleMonitor.enterIf(handleMonitor.newGuard(() -> !bucket.isEmpty()))) {
            try {
                rateLimiter.acquire();
                // 处理数据
                consumer.accept(bucket.poll());
            } finally {
                handleMonitor.leave();
            }
        }
    }

    /**
     * 请求类
     */
    static class Request {
        private final int data;

        public Request(int data) {
            this.data = data;
        }

        public int getData() {
            return data;
        }

        @Override
        public String toString() {
            return "Request{" +
                    "data=" + data +
                    '}';
        }
    }

}
