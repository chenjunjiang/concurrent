package com.chenjj.concurrent.activeObjects.general;

import com.chenjj.concurrent.activeObjects.standard.ActiveFuture;
import com.chenjj.concurrent.future.Future;

import java.lang.reflect.Method;

/**
 * 收集接口方法信息和具体的调用方法
 * 包可见，只在框架内部使用，不暴露
 */
class ActiveMessage {
    // 接口方法的参数
    private final Object[] objects;
    // 接口方法
    private final Method method;
    // 有返回值的方法，会返回ActiveFuture<?>类型
    private final ActiveFuture<Object> future;
    // 具体的Service接口实现
    private final Object service;

    private ActiveMessage(Builder builder) {
        this.objects = builder.objects;
        this.method = builder.method;
        this.future = builder.future;
        this.service = builder.service;
    }

    /**
     * 通过反射的方式调用接口的具体实现
     */
    public void execute() {
        try {
            // 执行接口方法
            Object result = method.invoke(service, objects);
            if (future != null) {
                // 如果是有返回值的接口方法，需要调用get获得最终结果
                Future<?> realFuture = (Future<?>) result;
                Object realResult = realFuture.get();
                future.finish(realResult);
            }
        } catch (Exception e) {
            // 如果发生异常，那么有返回值的方法将会显示指定结果为null，无返回值的接口方法会忽略
            if (future != null) {
                future.finish(null);
            }
        }
    }

    static class Builder {
        private Object[] objects;
        private Method method;
        private ActiveFuture<Object> future;
        private Object service;

        public Builder useMethod(Method method) {
            this.method = method;
            return this;
        }

        public Builder returnFuture(ActiveFuture<Object> future) {
            this.future = future;
            return this;
        }

        public Builder withObjects(Object[] objects) {
            this.objects = objects;
            return this;
        }

        public Builder forService(Object service) {
            this.service = service;
            return this;
        }

        public ActiveMessage build() {
            return new ActiveMessage(this);
        }
    }
}
