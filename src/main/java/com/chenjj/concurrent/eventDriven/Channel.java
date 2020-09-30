package com.chenjj.concurrent.eventDriven;

/**
 * Channel主要用于接受来自Event Loop分配的消息，每一个Channel负责处理一种类型的消息(这取决于你对消息如何分配)
 * 它就是处理Event的Handler
 *
 * @param <E>
 */
public interface Channel<E extends Message> {
    /**
     * 处理Message
     *
     * @param message
     */
    void dispatch(E message);
}
