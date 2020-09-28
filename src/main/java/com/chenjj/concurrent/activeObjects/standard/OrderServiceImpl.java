package com.chenjj.concurrent.activeObjects.standard;

import com.chenjj.concurrent.activeObjects.general.ActiveMethod;
import com.chenjj.concurrent.future.Future;
import com.chenjj.concurrent.future.FutureService;

import java.util.concurrent.TimeUnit;

public class OrderServiceImpl implements OrderService {
    /**
     * 根据订单编号查询订单明细，有返回值，返回值是我们自定义的Future
     *
     * @param orderId
     * @return
     */
    @Override
    public Future<String> findOrderDetails(long orderId) {
        return FutureService.<Long, String>newService().submit(input -> {
            try {
                TimeUnit.SECONDS.sleep(10);
                System.out.println("process the orderID->" + orderId);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "The order details information";
        }, orderId, null);
    }

    /**
     * 提交订单，没有返回值
     *
     * @param account
     * @param orderId
     */
    @Override
    public void order(String account, long orderId) {
        try {
            TimeUnit.SECONDS.sleep(10);
            System.out.println("process the order for account " + account + ", orderId " + orderId);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
