package com.chenjj.concurrent.eventBus;

/**
 * EventBus会将方法的调用交给Runnable接口执行，Runnable接口不能抛出checked异常，并且在每一个subscribe方法中，也不
 * 允许将异常抛出从而影响EventBus对后续Subscriber进行消息推送，但是异常信息又不能被忽略掉，因此注册一个异常回调接口
 * 就可以知道在进行消息推送的时候发送了什么
 */
public interface EventExceptionHandler {
    void handle(Throwable cause, EventContext context);
}
