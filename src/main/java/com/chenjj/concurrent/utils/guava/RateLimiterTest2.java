package com.chenjj.concurrent.utils.guava;

import com.google.common.util.concurrent.RateLimiter;

/**
 * 0.0
 * 1.994848
 * 0.997427
 * 1.001031
 * 从输出结果发现：第二次调用rateLimiter.acquire(2)方法时耗时为2秒，原因就是因为第一次的透支。
 */
public class RateLimiterTest2 {
    private static RateLimiter rateLimiter = RateLimiter.create(2.0d);

    public static void main(String[] args) {
        // 第一次就申请4个，这样会透支下一次的请求时间
        System.out.println(rateLimiter.acquire(4));
        System.out.println(rateLimiter.acquire(2));
        System.out.println(rateLimiter.acquire(2));
        System.out.println(rateLimiter.acquire(2));
    }
}
