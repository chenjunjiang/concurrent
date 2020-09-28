package com.chenjj.concurrent.activeObjects.standard;

import com.chenjj.concurrent.future.Future;

import java.util.Map;

public class FindOrderDetailsMessage extends MethodMessage {

    protected FindOrderDetailsMessage(Map<String, Object> params, OrderService orderService) {
        super(params, orderService);
    }

    @Override
    public void execute() {
        Future<String> realFuture = orderService.findOrderDetails((Long) params.get("orderId"));
        ActiveFuture<String> activeFuture = (ActiveFuture<String>) params.get("activeFuture");
        try {
            // 阻塞直到findOrderDetails执行结束
            String result = realFuture.get();
            // 将结果通过finish方法传递给activeFuture
            activeFuture.finish(result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
