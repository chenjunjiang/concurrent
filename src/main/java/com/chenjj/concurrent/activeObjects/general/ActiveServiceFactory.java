package com.chenjj.concurrent.activeObjects.general;

import com.chenjj.concurrent.activeObjects.standard.ActiveFuture;
import com.chenjj.concurrent.future.Future;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 通用的Active Objects设计消除了为每一个接口方法定义MethodMessage的过程，同时也摒弃掉为每一个接口创建Proxy，
 * 所有的操作都会被支持动态代理的ActiveServiceFactory所替代
 */
public class ActiveServiceFactory {
    // 存放ActiveMessage
    private final static ActiveMessageQueue queue = new ActiveMessageQueue();

    public static <T> T active(T instance) {
        // 生成Service的代理类
        Object proxy = Proxy.newProxyInstance(instance.getClass().getClassLoader(), instance.getClass().getInterfaces(),
                new ActiveInvocationHandler<>(instance));
        return (T) proxy;
    }

    private static class ActiveInvocationHandler<T> implements InvocationHandler {
        private final T instance;

        private ActiveInvocationHandler(T instance) {
            this.instance = instance;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            // 如果接口方法被ActiveMethod注解，则转换成ActiveMessage
            if (method.isAnnotationPresent(ActiveMethod.class)) {
                System.out.println("加入队列，异步执行......");
                // 检查该方法是否符合规范
                this.checkMethod(method);
                ActiveMessage.Builder builder = new ActiveMessage.Builder();
                builder.useMethod(method).withObjects(args).forService(instance);
                Object result = null;
                if (this.isReturnFutureType(method)) {
                    result = new ActiveFuture<>();
                    builder.returnFuture((ActiveFuture<Object>) result);
                }
                // 加入队列
                queue.offer(builder.build());
                return result;
            } else {
                System.out.println("没有加入队列，直接执行......");
                // 普通方法(没有被ActiveMethod注解)直接执行即可
                return method.invoke(instance, args);
            }
        }

        private void checkMethod(Method method) throws IllegalActiveMethod {
            // 有返回值，必须是ActiveFuture类型的返回值
            if (!isReturnVoidType(method) && !isReturnFutureType(method)) {
                throw new IllegalActiveMethod("the method [" + method.getName() + "] return type must  be void/Future");
            }
        }

        private boolean isReturnFutureType(Method method) {
            return method.getReturnType().isAssignableFrom(Future.class);
        }

        private boolean isReturnVoidType(Method method) {
            return method.getReturnType().equals(Void.TYPE);
        }
    }
}
