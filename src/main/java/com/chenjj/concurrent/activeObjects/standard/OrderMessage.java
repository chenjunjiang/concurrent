package com.chenjj.concurrent.activeObjects.standard;

import java.util.Map;

public class OrderMessage extends MethodMessage {
    protected OrderMessage(Map<String, Object> params, OrderService orderService) {
        super(params, orderService);
    }

    @Override
    public void execute() {
        String account = (String) params.get("account");
        long orderId = (long) params.get("orderId");
        // 执行真正的order方法
        orderService.order(account, orderId);
    }
}
