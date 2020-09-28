package com.chenjj.concurrent.activeObjects.general;

import com.chenjj.concurrent.activeObjects.standard.OrderService;
import com.chenjj.concurrent.activeObjects.standard.OrderServiceImpl;
import com.chenjj.concurrent.future.Future;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        OrderService orderService = ActiveServiceFactory.active(new OrderServiceImpl());
        Future<String> future = orderService.findOrderDetails(123456);
        System.out.println("当前线程马上执行......");
        System.out.println(future.get());
    }
}
