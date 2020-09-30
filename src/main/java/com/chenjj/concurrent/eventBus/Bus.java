package com.chenjj.concurrent.eventBus;

public interface Bus {
    /**
     * 将某个对象注册到Bus上，从此以后改类就成为Subscriber了
     *
     * @param subscriber
     */
    void register(Object subscriber);

    /**
     * 将某个对象从到Bus上取消注册，不会再接收来自Bus的任何消息
     *
     * @param subscriber
     */
    void unregister(Object subscriber);

    /**
     * 提交Event到默认的topic
     *
     * @param event
     */
    void post(Object event);

    /**
     * 提交Event到指定的topic
     *
     * @param event
     */
    void post(Object event, String topic);

    /**
     * 关闭Bus
     */
    void close();

    /**
     * 返回Bus的名称
     *
     * @return
     */
    String getBusName();
}
