package com.chenjj.concurrent.eventDriven;

import java.util.HashMap;
import java.util.Map;

/**
 * 对DynamicRouter的基本实现，适合在单线程下使用，因此不需要考虑线程安全问题
 */
public class EventDispatcher implements DynamicRouter<Message> {
    // 用于保存Message和Channel之间的关系
    private final Map<Class<? extends Message>, Channel> routerTable;

    public EventDispatcher() {
        this.routerTable = new HashMap<>();
    }

    @Override
    public void registerChannel(Class<? extends Message> messageType, Channel<? extends Message> channel) {
        this.routerTable.put(messageType, channel);
    }

    @Override
    public void dispatch(Message message) {
        if (routerTable.containsKey(message.getType())) {
            // 处理消息
            routerTable.get(message.getType()).dispatch(message);
        } else {
            throw new MessageMatcherException("Can't match the channel for [" + message.getType() + "] type");
        }
    }
}
