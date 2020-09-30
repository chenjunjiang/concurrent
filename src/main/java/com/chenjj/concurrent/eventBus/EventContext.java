package com.chenjj.concurrent.eventBus;

import java.lang.reflect.Method;

/**
 * 提供获取消息源、消息体，以及该消息是由哪一个Subscriber的哪个subscribe方法所接受
 */
public interface EventContext {
    String getSource();

    Object getSubscriber();

    Method getSubscribe();

    Object getEvent();
}
