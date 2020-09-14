package com.chenjj.concurrent.future;

import java.util.concurrent.TimeUnit;

public class FutureTest {
    public static void main(String[] args) throws InterruptedException {
        // 不需要返回值
        /*FutureService<Void, Void> futureService = new FutureServiceImpl<>();
        Future<?> future = futureService.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("I am finish done.");
        });
        future.get();
        System.out.println("等待提交的任务执行完毕......");*/

        // 需要返回值
        /*FutureService<String, Integer> futureService = new FutureServiceImpl<>();
        Future<Integer> future = futureService.submit(input -> {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("I am finish done.");
            return input.length();
        }, "Hello");
        int result = future.get();
        System.out.println("等待提交的任务执行完毕......，结果是: " + result);*/

        // 以上两种方式虽然提交任务的时候不阻塞，但是当调用者需要获取结果的时候，还是有可能陷入阻塞直到任务完成。

        // 下面通过回调的方式来解决阻塞的问题
        FutureService<String, Integer> futureService = new FutureServiceImpl<>();
        Future<Integer> future = futureService.submit(input -> {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("I am finish done.");
            return input.length();
        }, "Hello", (result -> {
            System.out.println("任务执行的结果是: " + result);
        }));
        System.out.println("继续做其它事情......");
        // 当然也可以继续使用future.get()获取结果
    }
}
