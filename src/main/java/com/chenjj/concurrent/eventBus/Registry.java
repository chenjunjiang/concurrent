package com.chenjj.concurrent.eventBus;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Subscriber注册表
 * 维护topic和subscriber之间的关系，当有Event被post之后，Dispatcher需要知道该消息应该发送给哪个Subscriber的实例和对应的
 * 方法，Subscriber
 */
class Registry {
    // 存储Subscriber集合和topic之间关系的map
    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<Subscriber>> subscriberContainer
            = new ConcurrentHashMap<>();

    public void bind(Object subscriber) {
        // 获取Subscriber Object的方法集合然后进行绑定
        List<Method> subscribeMethods = getSubscribeMethods(subscriber);
        subscribeMethods.forEach(m -> tierSubscriber(subscriber, m));
    }

    public void unbind(Object subscriber) {
        // 为了提高效率，只对Subscriber进行失效操作
        subscriberContainer.forEach((key, queue) -> {
            queue.forEach(s -> {
                if (s.getSubscribeObject() == subscriber) {
                    s.setDisable(true);
                }
            });
        });
    }

    public ConcurrentLinkedQueue<Subscriber> scanSubscriber(final String topic) {
        return subscriberContainer.get(topic);
    }

    private void tierSubscriber(Object subscriber, Method method) {
        final Subscribe subscribe = method.getDeclaredAnnotation(Subscribe.class);
        String topic = subscribe.topic();
        // 当某个topic没有queue的时候就创建一个
        subscriberContainer.computeIfAbsent(topic, key -> new ConcurrentLinkedQueue<>());
        // 创建一个Subscriber并加入到Subscriber列表中
        subscriberContainer.get(topic).add(new Subscriber(subscriber, method));
    }

    private List<Method> getSubscribeMethods(Object subscriber) {
        final List<Method> methods = new ArrayList<>();
        Class<?> clazz = subscriber.getClass();
        // 不断获取当前类和父类所有有@Subscribe的方法
        while (clazz != null) {
            Method[] declaredMethods = clazz.getDeclaredMethods();
            // public方法&&有一个入参&&方法上有@Subscribe
            Arrays.stream(declaredMethods).filter(m -> m.isAnnotationPresent(Subscribe.class)
                    && m.getParameterCount() == 1 && m.getModifiers() == Modifier.PUBLIC).forEach(methods::add);
            clazz = clazz.getSuperclass();
        }
        return methods;
    }
}
