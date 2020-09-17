package com.chenjj.concurrent.threadContext;

/**
 * 使用ThreadLocal实现一个简单的线程上下文
 * 所有的业务数据封装在一个ThreadLocal里面
 */
public class TaskContext {
    private static final ThreadLocal<Context> context = ThreadLocal.withInitial(Context::new);

    public static Context get() {
        return context.get();
    }

    /**
     * 每一个线程都会有一个独立的Context实例
     */
    static class Context {
        private Configuration configuration;
        private OtherResource otherResource;

        public Configuration getConfiguration() {
            return configuration;
        }

        public void setConfiguration(Configuration configuration) {
            this.configuration = configuration;
        }

        public OtherResource getOtherResource() {
            return otherResource;
        }

        public void setOtherResource(OtherResource otherResource) {
            this.otherResource = otherResource;
        }
    }

}
