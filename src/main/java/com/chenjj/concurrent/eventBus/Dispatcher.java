package com.chenjj.concurrent.eventBus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/**
 * Dispatcher作用是將EventBus post的event推送给每一个注册到topic的subscriber上，具体的推送其实就是执行@Subscribe的方法
 */
public class Dispatcher {
    private final Executor executorService;
    private final EventExceptionHandler eventExceptionHandler;

    public static final Executor SEQ_EXECUTOR_SERVICE = SeqExecutorService.INSTANCE;
    public static final Executor PRE_THREAD_EXECUTOR_SERVICE = PreThreadExecutorService.INSTANCE;

    private Dispatcher(Executor executor, EventExceptionHandler eventExceptionHandler) {
        this.executorService = executor;
        this.eventExceptionHandler = eventExceptionHandler;
    }

    public void dispatch(Bus bus, Registry registry, Object event, String topic) {
        ConcurrentLinkedQueue<Subscriber> subscribers = registry.scanSubscriber(topic);
        if (subscribers == null) {
            if (eventExceptionHandler != null) {
                eventExceptionHandler.handle(new IllegalArgumentException("The topic" + topic + " not bind yet")
                        , new BaseEventContext(bus.getBusName(), null, event));
            }
            return;
        }
        // 遍历所有的方法，并且通过反射进行方法调用
        subscribers.stream().filter(subscriber -> !subscriber.isDisable()).filter(subscriber -> {
            Method subscribeMethod = subscriber.getSubscribeMethod();
            Class<?> aClass = subscribeMethod.getParameterTypes()[0];
            return aClass.isAssignableFrom(event.getClass());
        }).forEach(subscriber -> realInvokeSubscribe(subscriber, event, bus));
    }

    public void close() {
        if (executorService instanceof ExecutorService) {
            ((ExecutorService) executorService).shutdown();
        }
    }

    static Dispatcher newDispatcher(EventExceptionHandler eventExceptionHandler, Executor executor) {
        return new Dispatcher(executor, eventExceptionHandler);
    }

    private void realInvokeSubscribe(Subscriber subscriber, Object event, Bus bus) {
        Method subscribeMethod = subscriber.getSubscribeMethod();
        Object subscribeObject = subscriber.getSubscribeObject();
        executorService.execute(() -> {
            try {
                subscribeMethod.invoke(subscribeObject, event);
            } catch (Exception e) {
                if (eventExceptionHandler != null) {
                    eventExceptionHandler.handle(e, new BaseEventContext(bus.getBusName(),
                            subscriber, event));
                }
            }
        });
    }

    private static class SeqExecutorService implements Executor {
        private final static SeqExecutorService INSTANCE = new SeqExecutorService();

        @Override
        public void execute(Runnable command) {
            command.run();
        }
    }

    private static class PreThreadExecutorService implements Executor {
        private final static PreThreadExecutorService INSTANCE = new PreThreadExecutorService();

        @Override
        public void execute(Runnable command) {
            new Thread(command).start();
        }
    }

    private static class BaseEventContext implements EventContext {
        private final String eventBusName;
        private final Subscriber subscriber;
        private final Object event;

        private BaseEventContext(String eventBusName, Subscriber subscriber, Object event) {
            this.eventBusName = eventBusName;
            this.subscriber = subscriber;
            this.event = event;
        }

        @Override
        public String getSource() {
            return this.eventBusName;
        }

        @Override
        public Object getSubscriber() {
            return this.subscriber != null ? subscriber.getSubscribeObject() : null;
        }

        @Override
        public Method getSubscribe() {
            return this.subscriber != null ? subscriber.getSubscribeMethod() : null;
        }

        @Override
        public Object getEvent() {
            return this.event;
        }
    }
}
