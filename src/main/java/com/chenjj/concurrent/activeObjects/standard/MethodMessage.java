package com.chenjj.concurrent.activeObjects.standard;

import java.util.Map;

/**
 * 收集每一个接口的方法参数
 */
public abstract class MethodMessage {
    protected final Map<String, Object> params;
    protected final OrderService orderService;

    protected MethodMessage(Map<String, Object> params, OrderService orderService) {
        this.params = params;
        this.orderService = orderService;
    }

    public abstract void execute();
}
