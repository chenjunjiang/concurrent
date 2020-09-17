package com.chenjj.concurrent.threadContext;

/**
 * 为每一个业务数据定义一个ThreadLocal
 */
public class TaskContext1 {
    private static final ThreadLocal<Configuration> configuration = ThreadLocal.withInitial(Configuration::new);
    private static final ThreadLocal<OtherResource> otherResource = ThreadLocal.withInitial(OtherResource::new);

    public static void setConfiguration(Configuration conf) {
        configuration.set(conf);
    }

    public static Configuration getConfiguration() {
        return configuration.get();
    }

    public static void setOtherResource(OtherResource resource) {
        otherResource.set(resource);
    }

    public static OtherResource getOtherResource() {
        return otherResource.get();
    }
}
