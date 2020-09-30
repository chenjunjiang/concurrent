package com.chenjj.concurrent.eventDriven;

/**
 * Router的作用类似于Event Loop，主要是帮助Event找到合适的Channel并且传送给它
 */
public interface DynamicRouter<E extends Message> {
    /**
     * 针对每一种Message类型注册相关的Channel，只有找到合适的Channel，该Message才会被处理
     *
     * @param messageType
     * @param channel
     */
    void registerChannel(Class<? extends E> messageType, Channel<? extends E> channel);

    /**
     * 为message找到对应的Channel进行处理
     *
     * @param message
     */
    void dispatch(E message);
}
