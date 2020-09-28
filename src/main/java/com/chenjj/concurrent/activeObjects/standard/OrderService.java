package com.chenjj.concurrent.activeObjects.standard;

import com.chenjj.concurrent.activeObjects.general.ActiveMethod;
import com.chenjj.concurrent.future.Future;

public interface OrderService {
    /**
     * 根据订单编号查询订单明细，有返回值，返回值是我们自定义的Future
     *
     * @param orderId
     * @return
     */
    Future<String> findOrderDetails(long orderId);

    /**
     * 提交订单，没有返回值
     *
     * @param account
     * @param orderId
     */
    void order(String account, long orderId);
}
