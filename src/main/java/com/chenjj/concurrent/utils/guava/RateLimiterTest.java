package com.chenjj.concurrent.utils.guava;

import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.TimeUnit;

/**
 * 假设我们只允许某个方法在单位时间内（1秒）被调用0.5次，也就是说该方法的访问速率为0.5/秒，
 * 即2秒内只允许有一次对该方法的访问操作
 */
public class RateLimiterTest {
    private static RateLimiter rateLimiter = RateLimiter.create(0.5);

    public static void main(String[] args) {
        /*for (; ; ) {
            testRateLimiter();
        }*/
        for (int i = 0; i < 10; i++) {
            new Thread(RateLimiterTest::testRateLimiter).start();
        }
    }

    private static void testRateLimiter() {
        // 返回实际等待的时间
        double elapsedSecond = rateLimiter.acquire();
        System.out.println(Thread.currentThread() + ":elapsed seconds:" + elapsedSecond);
        // elapsedSecond的值不会受到下面休眠时间的影响
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
