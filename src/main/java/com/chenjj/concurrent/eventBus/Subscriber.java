package com.chenjj.concurrent.eventBus;

import java.lang.reflect.Method;

/**
 * 封装对象实例和被@Subscribe标记的方法，也就是说一个对象实例有可能被封装成若干个Subscriber
 */
public class Subscriber {
    private final Object subscribeObject;
    private final Method subscribeMethod;
    private boolean disable = false;

    public Subscriber(Object subscribeObject, Method subscribeMethod) {
        this.subscribeObject = subscribeObject;
        this.subscribeMethod = subscribeMethod;
    }

    public Object getSubscribeObject() {
        return subscribeObject;
    }

    public Method getSubscribeMethod() {
        return subscribeMethod;
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
    }

    public boolean isDisable() {
        return disable;
    }
}
