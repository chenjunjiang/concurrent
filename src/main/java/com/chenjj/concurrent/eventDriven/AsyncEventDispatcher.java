package com.chenjj.concurrent.eventDriven;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 线程安全
 */
public class AsyncEventDispatcher implements DynamicRouter<Event> {
    private final Map<Class<? extends Event>, Channel> routerTable;

    public AsyncEventDispatcher() {
        this.routerTable = new ConcurrentHashMap<>();
    }

    @Override
    public void registerChannel(Class<? extends Event> messageType, Channel<? extends Event> channel) {
        // channel必须是AsyncChannel类型
        if (!(channel instanceof AsyncChannel)) {
            throw new IllegalArgumentException("The channel must be AsyncChannel Type.");
        }
        this.routerTable.put(messageType, channel);
    }

    @Override
    public void dispatch(Event message) {
        if (routerTable.containsKey(message.getType())) {
            routerTable.get(message.getType()).dispatch(message);
        } else {
            throw new MessageMatcherException("Can't match the channel for [" + message.getType() + "] type");
        }
    }
}
