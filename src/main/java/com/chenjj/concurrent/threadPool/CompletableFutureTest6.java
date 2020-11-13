package com.chenjj.concurrent.threadPool;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 优雅处理异常
 */
public class CompletableFutureTest6 {
    public static void main(String[] args) throws InterruptedException {
        CompletableFuture.supplyAsync(() -> {
            // 这里不能抛出checked异常
            throw new RuntimeException("发生异常了......");
        }).handle((r, e) -> {
            if (e != null) {
                e.printStackTrace();
                return "ERROR";
            } else {
                return r;
            }
        }).thenAccept(System.out::println);
        TimeUnit.SECONDS.sleep(10);
    }
}
