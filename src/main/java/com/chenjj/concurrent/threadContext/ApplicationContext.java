package com.chenjj.concurrent.threadContext;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 上下文是贯穿整个系统或阶段生命周期的对象，其中包含了系统全局的一些信息，比如登录之后的用户信息、账号信息，以及在程序
 * 每一个阶段运行时的数据。比如Spring中的ApplicationContext。
 * 下面这个例子使用单例对象充当系统级别上下文，如果configuration和runtimeInfo的生命周期会从创建开始一直到系统运行
 * 结束而结束，就可以将ApplicationContext称为系统的上下文，configuration和runtimeInfo实例属性则称为系统上下文成员。
 * 在设计系统上下文时，处理要考虑它的全局唯一性，还要考虑到有些成员只能被初始化一次，比如配置信息的加载，以及在多线程环境
 * 下，上下文成员的线程安全性。
 */
public final class ApplicationContext {
    // 在Context中保存configuration实例
    private ApplicationConfiguration configuration;
    // 在Context中保存runtimeInfo实例
    private RuntimeInfo runtimeInfo;

    /**
     * ActionContext作为线程上下文，每一个线程都有它自己的上下文。
     * 通过这种方式定义线程上下文很可能会导致内存泄露，contexts是一个Map的数据结构，用当前线程做key，当线程的生命周期结束
     * 后，contexts中的Thread实例不会被释放，与之对应的Value也不会被释放，时间长了就会导致内存泄露，当然可以通过soft reference
     * 或weak reference引用类型，JVM会主动尝试回收；还有一种办法就是当线程的生命周期结束时把key和value从contexts中删除。
     */
    private ConcurrentHashMap<Thread, ActionContext> contexts = new ConcurrentHashMap<>();

    // 采用Holder的方式实现单例
    private static class Holder {
        private static ApplicationContext instance = new ApplicationContext();
    }

    public static ApplicationContext getContext() {
        return Holder.instance;
    }

    public ActionContext getActionContext() {
        ActionContext actionContext = contexts.get(Thread.currentThread());
        if (actionContext == null) {
            actionContext = new ActionContext();
            contexts.put(Thread.currentThread(), actionContext);
        }
        return actionContext;
    }

    public ApplicationConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(ApplicationConfiguration configuration) {
        this.configuration = configuration;
    }

    public RuntimeInfo getRuntimeInfo() {
        return runtimeInfo;
    }

    public void setRuntimeInfo(RuntimeInfo runtimeInfo) {
        this.runtimeInfo = runtimeInfo;
    }
}
