package com.chenjj.concurrent.metrics.plugin;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.health.jvm.ThreadDeadlockHealthCheck;

import java.util.SortedMap;
import java.util.concurrent.TimeUnit;

/**
 * 20-11-18 16:23:35 ==============================================================
 *
 * -- Gauges ----------------------------------------------------------------------
 * thread-dead-lock-hc
 *              value = {thread-dead-lock-hc=Result{isHealthy=true, timestamp=2020-11-18T16:23:35.631+0800}}
 */
public class DeadLockHealthCheckTest {
    public static void main(String[] args) throws InterruptedException {
        /**
         * 定义一个HealthCheckRegistry，这个Registry只能存放HealthCheck类的子类，无论是HealthCheckRegistry
         * 还是HealthCheck都与Metrics核心框架没有多大的关系，虽然HealthCheckRegistry看起来也是一个Registry，
         * 会让人误以为是MetricRegistry的子类或者扩充类什么的。
         */
        final HealthCheckRegistry healthCheckRegistry = new HealthCheckRegistry();
        /**
         * 我们使用ThreadDeadlockHealthCheck进行当前JVM是否出现了死锁的健康检查。
         * 将ThreadDeadlockHealthCheck注册至hcRegistry中，该类是一个HealthCheck的实现，其内部原理就是通过JVM MBean
         * 的形式获得当前JVM的线程堆栈。
         */
        healthCheckRegistry.register("thread-dead-lock-hc", new ThreadDeadlockHealthCheck());
        final MetricRegistry registry = new MetricRegistry();
        final ConsoleReporter reporter = ConsoleReporter.forRegistry(registry).build();
        // 执行HealthCheckRegistry中的所有hc，并将结果作为Gauge
        /**
         * 想要Health Check的执行以及输出被Metrics接管，我们需要将其转换为某个Metric，将Health Check Registry中的
         * 所有Health Check实例都执行了一遍，最后将结果作为一个Gauge Metric传递给Metrics Registry，这种方式非常
         * 巧妙地制造了它们之间的连接。
         */
        registry.register("thread-dead-lock-hc", (Gauge<SortedMap<String, HealthCheck.Result>>) () -> healthCheckRegistry.runHealthChecks());
        reporter.start(10, TimeUnit.SECONDS);
        Thread.currentThread().join();
    }
}
