package com.chenjj.concurrent.activeObjects.standard;

import com.chenjj.concurrent.future.Future;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        OrderService orderService = OrderServiceFactory.toActiveObject(new OrderServiceImpl());
        /*orderService.order("hello", 123456);
        System.out.println("不用等order方法执行完毕就执行......");*/
        Future<String> future = orderService.findOrderDetails(123456);
        System.out.println("不用等findOrderDetails方法执行完毕就执行......");
        String result = future.get();
        System.out.println(result);
        Thread.currentThread().join();
    }
}
