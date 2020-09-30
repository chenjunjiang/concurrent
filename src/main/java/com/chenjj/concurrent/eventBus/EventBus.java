package com.chenjj.concurrent.eventBus;

import java.util.concurrent.Executor;

/**
 * 同步Bus
 * 对Event的广播推送采用同步的方式
 */
public class EventBus implements Bus {
    // 用于维护subscriber的注册表
    private final Registry registry = new Registry();
    private String busName;
    private final static String DEFAULT_BUS_NAME = "default";
    private final static String DEFAULT_TOPIC = "default-topic";
    private final Dispatcher dispatcher;

    public EventBus() {
        this(DEFAULT_BUS_NAME, null, Dispatcher.SEQ_EXECUTOR_SERVICE);
    }

    public EventBus(String busName) {
        this(busName, null, Dispatcher.SEQ_EXECUTOR_SERVICE);
    }

    public EventBus(String busName, EventExceptionHandler eventExceptionHandler, Executor executor) {
        this.busName = busName;
        this.dispatcher = Dispatcher.newDispatcher(eventExceptionHandler, executor);
    }

    public EventBus(EventExceptionHandler eventExceptionHandler) {
        this(DEFAULT_BUS_NAME, eventExceptionHandler, Dispatcher.SEQ_EXECUTOR_SERVICE);
    }

    /**
     * 将某个对象注册到Bus上，从此以后改类就成为Subscriber了
     *
     * @param subscriber
     */
    @Override
    public void register(Object subscriber) {
        this.registry.bind(subscriber);
    }

    /**
     * 将某个对象从到Bus上取消注册，不会再接收来自Bus的任何消息
     *
     * @param subscriber
     */
    @Override
    public void unregister(Object subscriber) {
        this.registry.unbind(subscriber);
    }

    /**
     * 提交Event到默认的topic
     *
     * @param event
     */
    @Override
    public void post(Object event) {
        this.post(event, DEFAULT_TOPIC);
    }

    /**
     * 提交Event到指定的topic
     *
     * @param event
     * @param topic
     */
    @Override
    public void post(Object event, String topic) {
        this.dispatcher.dispatch(this, registry, event, topic);
    }

    /**
     * 关闭Bus
     */
    @Override
    public void close() {
        this.dispatcher.close();
    }

    /**
     * 返回Bus的名称
     *
     * @return
     */
    @Override
    public String getBusName() {
        return this.busName;
    }
}
